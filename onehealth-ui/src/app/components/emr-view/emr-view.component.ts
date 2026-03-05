import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, PatientDTO } from '../../services/api.service';

@Component({
  selector: 'app-emr-view',
  standalone: true,
  imports: [CommonModule],
  template: `
    <h2>EMR — Registered Patients (Current Tenant)</h2>
    <button class="btn" (click)="loadPatients()">Refresh</button>
    <div *ngIf="loading" class="loading">Loading...</div>
    <table *ngIf="patients.length > 0" class="data-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>First Name</th>
          <th>Last Name</th>
          <th>Gender</th>
          <th>Date of Birth</th>
          <th>National ID</th>
          <th>Phone</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let p of patients">
          <td>{{ p.id | slice:0:8 }}...</td>
          <td>{{ p.firstName }}</td>
          <td>{{ p.lastName }}</td>
          <td>{{ p.gender }}</td>
          <td>{{ p.dateOfBirth }}</td>
          <td>{{ p.nationalId }}</td>
          <td>{{ p.phoneNumber }}</td>
        </tr>
      </tbody>
    </table>
    <div *ngIf="!loading && patients.length === 0" class="empty">No patients found for current tenant.</div>
    <div *ngIf="error" class="message error">{{ error }}</div>
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
    .loading, .empty { padding: 24px; color: #666; }
    .message.error { margin-top: 16px; padding: 12px; background: #ffebee; color: #c62828; border-radius: 4px; }
  `]
})
export class EmrViewComponent implements OnInit {

  patients: PatientDTO[] = [];
  loading = false;
  error = '';

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.loadPatients();
  }

  loadPatients(): void {
    this.loading = true;
    this.error = '';
    this.api.getPatients().subscribe({
      next: (data) => {
        this.patients = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = `Error loading patients: ${err.message}`;
        this.loading = false;
      }
    });
  }
}
