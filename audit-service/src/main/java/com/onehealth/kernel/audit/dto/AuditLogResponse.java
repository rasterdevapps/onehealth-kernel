package com.onehealth.kernel.audit.dto;

import java.time.Instant;
import java.util.UUID;

public record AuditLogResponse(
        UUID id,
        String tenantId,
        String actionType,
        String entityType,
        String entityId,
        String userId,
        String username,
        String ipAddress,
        String userAgent,
        String details,
        String outcome,
        Instant occurredAt
) {}
