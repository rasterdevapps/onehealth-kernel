import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, LabOrderDTO } from '../../services/api.service';

@Component({
  selector: 'app-lis-view',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>LIS — Lab Orders</h2>
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
export class LisViewComponent implements OnInit {

  orders: LabOrderDTO[] = [];
  message = '';
  isError = false;

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.loadOrders();
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
        this.message = 'Lab test executed successfully!';
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
