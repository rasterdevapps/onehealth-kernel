package com.onehealth.kernel.authorization.dto;

import java.time.Instant;
import java.util.UUID;

public record RoleResponse(
        UUID id,
        String name,
        String description,
        boolean systemRole,
        String tenantId,
        Instant createdAt
) {}
