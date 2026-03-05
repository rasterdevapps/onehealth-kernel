import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PatientDTO {
  id?: string;
  tenantId?: string;
  firstName: string;
  lastName: string;
  dateOfBirth?: string;
  gender?: string;
  nationalId?: string;
  phoneNumber?: string;
  email?: string;
}

export interface EncounterDTO {
  id?: string;
  tenantId?: string;
  patientId: string;
  patientName?: string;
  encounterType?: string;
  diagnosis?: string;
  notes?: string;
  status?: string;
  encounterDate?: string;
}

export interface LabOrderDTO {
  id?: string;
  tenantId?: string;
  patientId: string;
  encounterId?: string;
  testType: string;
  status?: string;
  result?: string;
  orderedAt?: string;
  completedAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private gatewayUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // HIS - Patient endpoints
  registerPatient(patient: PatientDTO): Observable<PatientDTO> {
    return this.http.post<PatientDTO>(`${this.gatewayUrl}/api/patients`, patient);
  }

  getPatients(): Observable<PatientDTO[]> {
    return this.http.get<PatientDTO[]>(`${this.gatewayUrl}/api/patients`);
  }

  // EMR - Encounter endpoints
  createEncounter(encounter: EncounterDTO): Observable<EncounterDTO> {
    return this.http.post<EncounterDTO>(`${this.gatewayUrl}/api/encounters`, encounter);
  }

  getEncounters(): Observable<EncounterDTO[]> {
    return this.http.get<EncounterDTO[]>(`${this.gatewayUrl}/api/encounters`);
  }

  updateEncounterStatus(id: string, status: string): Observable<EncounterDTO> {
    return this.http.patch<EncounterDTO>(`${this.gatewayUrl}/api/encounters/${id}/status?status=${status}`, {});
  }

  // LIS - Lab Order endpoints
  createLabOrder(order: LabOrderDTO): Observable<LabOrderDTO> {
    return this.http.post<LabOrderDTO>(`${this.gatewayUrl}/api/lab-orders`, order);
  }

  executeLabTest(orderId: string): Observable<LabOrderDTO> {
    return this.http.put<LabOrderDTO>(`${this.gatewayUrl}/api/lab-orders/${orderId}/execute`, {});
  }

  getLabOrders(): Observable<LabOrderDTO[]> {
    return this.http.get<LabOrderDTO[]>(`${this.gatewayUrl}/api/lab-orders`);
  }
}
