import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, PatientDTO } from '../../services/api.service';

@Component({
  selector: 'app-his-view',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <h2>HIS — Patient Registration</h2>
    <div class="form-section">
      <h3>Register New Patient</h3>
      <form (ngSubmit)="registerPatient()">
        <div class="form-row">
          <div class="form-group">
            <label>First Name *</label>
            <input [(ngModel)]="patient.firstName" name="firstName" required />
          </div>
          <div class="form-group">
            <label>Last Name *</label>
            <input [(ngModel)]="patient.lastName" name="lastName" required />
          </div>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label>Date of Birth</label>
            <input type="date" [(ngModel)]="patient.dateOfBirth" name="dateOfBirth" />
          </div>
          <div class="form-group">
            <label>Gender</label>
            <select [(ngModel)]="patient.gender" name="gender">
              <option value="">Select</option>
              <option value="MALE">Male</option>
              <option value="FEMALE">Female</option>
              <option value="OTHER">Other</option>
            </select>
          </div>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label>National ID</label>
            <input [(ngModel)]="patient.nationalId" name="nationalId" />
          </div>
          <div class="form-group">
            <label>Phone</label>
            <input [(ngModel)]="patient.phoneNumber" name="phoneNumber" />
          </div>
        </div>
        <div class="form-group">
          <label>Email</label>
          <input type="email" [(ngModel)]="patient.email" name="email" />
        </div>
        <button type="submit" class="btn">Register Patient</button>
      </form>
      <div *ngIf="message" class="message" [class.error]="isError">{{ message }}</div>
    </div>
  `,
  styles: [`
    h2 { color: #1a237e; }
    .form-section {
      background: white;
      border: 1px solid #e0e0e0;
      border-radius: 8px;
      padding: 24px;
      max-width: 700px;
    }
    .form-row { display: flex; gap: 16px; }
    .form-group { flex: 1; margin-bottom: 16px; }
    label { display: block; font-size: 13px; color: #555; margin-bottom: 4px; }
    input, select {
      width: 100%;
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 4px;
      font-size: 14px;
      box-sizing: border-box;
    }
    .btn {
      padding: 10px 24px;
      background: #1a237e;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-size: 14px;
    }
    .btn:hover { background: #283593; }
    .message { margin-top: 16px; padding: 12px; border-radius: 4px; background: #e8f5e9; color: #2e7d32; }
    .message.error { background: #ffebee; color: #c62828; }
  `]
})
export class HisViewComponent {

  patient: PatientDTO = { firstName: '', lastName: '' };
  message = '';
  isError = false;

  constructor(private api: ApiService) {}

  registerPatient(): void {
    this.api.registerPatient(this.patient).subscribe({
      next: (result) => {
        this.message = `Patient registered successfully! ID: ${result.id}`;
        this.isError = false;
        this.patient = { firstName: '', lastName: '' };
      },
      error: (err) => {
        this.message = `Error: ${err.error?.message || err.message || 'Registration failed'}`;
        this.isError = true;
      }
    });
  }
}
