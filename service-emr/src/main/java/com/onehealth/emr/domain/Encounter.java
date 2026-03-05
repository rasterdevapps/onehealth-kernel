package com.onehealth.emr.domain;

import com.onehealth.kernel.auth.TenantAware;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "emr_encounter")
public class Encounter extends TenantAware {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "encounter_type")
    private String encounterType;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "notes", length = 2000)
    private String notes;

    @Column(name = "status")
    private String status;

    @Column(name = "encounter_date")
    private Instant encounterDate;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getPatientId() { return patientId; }
    public void setPatientId(UUID patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getEncounterType() { return encounterType; }
    public void setEncounterType(String encounterType) { this.encounterType = encounterType; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getEncounterDate() { return encounterDate; }
    public void setEncounterDate(Instant encounterDate) { this.encounterDate = encounterDate; }
}
