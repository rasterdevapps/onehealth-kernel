package com.onehealth.erp.common.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class LabResultReadyEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID orderId;
    private UUID patientId;
    private String tenantId;
    private String testType;
    private String result;
    private Instant completedAt;

    public LabResultReadyEvent() {}

    public LabResultReadyEvent(UUID orderId, UUID patientId, String tenantId,
                               String testType, String result) {
        this.orderId = orderId;
        this.patientId = patientId;
        this.tenantId = tenantId;
        this.testType = testType;
        this.result = result;
        this.completedAt = Instant.now();
    }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getPatientId() { return patientId; }
    public void setPatientId(UUID patientId) { this.patientId = patientId; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getTestType() { return testType; }
    public void setTestType(String testType) { this.testType = testType; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
