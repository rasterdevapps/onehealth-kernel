package com.onehealth.erp.common.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class LabOrderCreatedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID orderId;
    private UUID patientId;
    private String tenantId;
    private String testType;
    private Instant createdAt;

    public LabOrderCreatedEvent() {}

    public LabOrderCreatedEvent(UUID orderId, UUID patientId, String tenantId, String testType) {
        this.orderId = orderId;
        this.patientId = patientId;
        this.tenantId = tenantId;
        this.testType = testType;
        this.createdAt = Instant.now();
    }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getPatientId() { return patientId; }
    public void setPatientId(UUID patientId) { this.patientId = patientId; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getTestType() { return testType; }
    public void setTestType(String testType) { this.testType = testType; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
