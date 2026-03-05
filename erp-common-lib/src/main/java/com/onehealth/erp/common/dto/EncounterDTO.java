package com.onehealth.erp.common.dto;

import java.time.Instant;
import java.util.UUID;

public class EncounterDTO {

    private UUID id;
    private String tenantId;
    private UUID patientId;
    private String patientName;
    private String encounterType;
    private String diagnosis;
    private String notes;
    private String status;
    private Instant encounterDate;

    public EncounterDTO() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

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
