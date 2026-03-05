import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { ApiService, LabOrderDTO } from '../../services/api.service';
import { WebSocketService, LabResultNotification } from '../../services/websocket.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-lis-view',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>LIS — Lab Orders</h2>

    <div *ngIf="notifications.length > 0" class="notification-panel">
      <h3>🔔 Real-Time Lab Result Notifications</h3>
      <div *ngFor="let n of notifications" class="notification-item">
        <strong>{{ n.testType }}</strong> — {{ n.result }}
        <span class="note">(Order: {{ n.orderId | slice:0:8 }}... | Completed: {{ n.completedAt | date:'HH:mm:ss' }})</span>
        <span class="ws-badge">LIVE</span>
      </div>
    </div>
    <div *ngIf="wsStatus" class="ws-status" [class.connected]="wsConnected">{{ wsStatus }}</div>

    <button class="btn" (click)="loadOrders()">Refresh</button>
    <table *ngIf="orders.length > 0" class="data-table">
      <thead>
        <tr>
          <th>Order ID</th>
          <th>Test Type</th>
          <th>Status</th>
          <th>Result</th>
          <th>Ordered At</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let o of orders">
          <td>{{ o.id | slice:0:8 }}...</td>
          <td>{{ o.testType }}</td>
          <td>
            <span class="status" [class.completed]="o.status === 'COMPLETED'"
                                  [class.ordered]="o.status === 'ORDERED'">
              {{ o.status }}
            </span>
          </td>
          <td>{{ o.result || '—' }}</td>
          <td>{{ o.orderedAt }}</td>
          <td>
            <button *ngIf="o.status === 'ORDERED'" class="btn-sm" (click)="executeTest(o.id!)">
              Execute Lab Test
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <div *ngIf="orders.length === 0" class="empty">No lab orders found.</div>
    <div *ngIf="message" class="message" [class.error]="isError">{{ message }}</div>
  `,
  styles: [`
    h2 { color: #1a237e; }
    .notification-panel {
      background: #e8f5e9;
      border: 1px solid #a5d6a7;
      border-radius: 8px;
      padding: 16px;
      margin-bottom: 16px;
    }
    .notification-panel h3 { color: #2e7d32; margin: 0 0 12px 0; font-size: 15px; }
    .notification-item {
      padding: 8px 0;
      border-bottom: 1px solid #c8e6c9;
      font-size: 14px;
      color: #1b5e20;
    }
    .notification-item:last-child { border-bottom: none; }
    .note { color: #555; margin-left: 8px; font-size: 12px; }
    .ws-badge {
      display: inline-block;
      background: #4caf50;
      color: white;
      padding: 2px 6px;
      border-radius: 10px;
      font-size: 10px;
      font-weight: 700;
      margin-left: 8px;
      animation: pulse 1.5s infinite;
    }
    @keyframes pulse { 0%, 100% { opacity: 1; } 50% { opacity: 0.5; } }
    .ws-status {
      font-size: 12px;
      color: #e65100;
      margin-bottom: 8px;
      padding: 4px 8px;
      background: #fff3e0;
      border-radius: 4px;
      display: inline-block;
    }
    .ws-status.connected { color: #2e7d32; background: #e8f5e9; }
    .btn {
      padding: 8px 20px;
      background: #1a237e;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      margin-bottom: 16px;
    }
    .btn:hover { background: #283593; }
    .btn-sm {
      padding: 6px 14px;
      background: #4caf50;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 13px;
    }
    .btn-sm:hover { background: #388e3c; }
    .data-table {
      width: 100%;
      border-collapse: collapse;
      background: white;
      border-radius: 8px;
      overflow: hidden;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .data-table th {
      background: #e8eaf6;
      color: #1a237e;
      padding: 12px;
      text-align: left;
      font-size: 13px;
    }
    .data-table td { padding: 10px 12px; border-bottom: 1px solid #eee; font-size: 14px; }
    .status { padding: 4px 8px; border-radius: 12px; font-size: 12px; font-weight: 600; }
    .status.ordered { background: #fff3e0; color: #e65100; }
    .status.completed { background: #e8f5e9; color: #2e7d32; }
    .empty { padding: 24px; color: #666; }
    .message { margin-top: 16px; padding: 12px; border-radius: 4px; background: #e8f5e9; color: #2e7d32; }
    .message.error { background: #ffebee; color: #c62828; }
  `]
})
export class LisViewComponent implements OnInit, OnDestroy {

  orders: LabOrderDTO[] = [];
  notifications: LabResultNotification[] = [];
  message = '';
  isError = false;
  wsStatus = 'Connecting to real-time updates...';
  wsConnected = false;

  private wsSub: Subscription | null = null;
  private readonly wsUrl = `${environment.gatewayWsUrl}/ws/lab-results`;

  constructor(private api: ApiService, private wsService: WebSocketService) {}

  ngOnInit(): void {
    this.loadOrders();
    this.connectWebSocket();
  }

  ngOnDestroy(): void {
    this.wsSub?.unsubscribe();
    this.wsService.disconnect();
  }

  private connectWebSocket(): void {
    try {
      this.wsService.connect(this.wsUrl);
      this.wsStatus = '⚡ Connected — receiving real-time lab results';
      this.wsConnected = true;
      this.wsSub = this.wsService.messages$.subscribe((notification) => {
        this.notifications.unshift(notification);
        this.loadOrders();
      });
    } catch (e) {
      this.wsStatus = 'Real-time updates unavailable (WebSocket not connected)';
      this.wsConnected = false;
    }
  }

  loadOrders(): void {
    this.api.getLabOrders().subscribe({
      next: (data) => this.orders = data,
      error: (err) => {
        this.message = `Error loading lab orders: ${err.message}`;
        this.isError = true;
      }
    });
  }

  executeTest(orderId: string): void {
    this.api.executeLabTest(orderId).subscribe({
      next: () => {
        this.message = 'Lab test executed — real-time notification incoming via WebSocket!';
        this.isError = false;
        this.loadOrders();
      },
      error: (err) => {
        this.message = `Error: ${err.message}`;
        this.isError = true;
      }
    });
  }
}
