package com.onehealth.kernel.authorization.dto;

import java.time.Instant;
import java.util.UUID;

public record PermissionResponse(
        UUID id,
        String name,
        String resource,
        String action,
        String description,
        String tenantId,
        Instant createdAt
) {}
