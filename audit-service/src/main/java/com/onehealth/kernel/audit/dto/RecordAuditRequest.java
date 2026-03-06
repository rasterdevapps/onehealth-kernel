package com.onehealth.kernel.audit.dto;

import jakarta.validation.constraints.NotBlank;

public record RecordAuditRequest(
        @NotBlank String actionType,
        String entityType,
        String entityId,
        String userId,
        String username,
        String ipAddress,
        String userAgent,
        String details,
        String outcome
) {}
