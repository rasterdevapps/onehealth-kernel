import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  template: `
    <h1>OneHealth ERP Dashboard</h1>
    <p>Kernel Orchestration Verification — HIS, EMR, LIS Starter Services</p>
    <div class="cards">
      <div class="card">
        <h2>HIS - Hospital Information System</h2>
        <p>Patient Registration with DDIC validation and tenant isolation.</p>
        <a routerLink="/his" class="btn">Open HIS</a>
      </div>
      <div class="card">
        <h2>EMR - Electronic Medical Records</h2>
        <p>Clinical Encounters linked to patients via Feign Client through Gateway.</p>
        <a routerLink="/emr" class="btn">Open EMR</a>
      </div>
      <div class="card">
        <h2>LIS - Laboratory Information System</h2>
        <p>Lab Orders with RabbitMQ event publishing on creation.</p>
        <a routerLink="/lis" class="btn">Open LIS</a>
      </div>
    </div>
  `,
  styles: [`
    h1 { color: #1a237e; margin-bottom: 4px; }
    p { color: #666; margin-bottom: 24px; }
    .cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 20px; }
    .card {
      border: 1px solid #e0e0e0;
      border-radius: 8px;
      padding: 24px;
      background: white;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .card h2 { color: #1a237e; font-size: 18px; margin-bottom: 8px; }
    .card p { font-size: 14px; }
    .btn {
      display: inline-block;
      padding: 8px 20px;
      background: #1a237e;
      color: white;
      text-decoration: none;
      border-radius: 4px;
      margin-top: 8px;
    }
    .btn:hover { background: #283593; }
  `]
})
export class DashboardComponent {}
