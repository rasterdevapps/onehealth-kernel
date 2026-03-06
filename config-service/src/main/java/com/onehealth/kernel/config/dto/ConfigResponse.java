package com.onehealth.kernel.config.dto;

import com.onehealth.kernel.config.model.ConfigScope;

import java.time.Instant;
import java.util.UUID;

public record ConfigResponse(
        UUID id,
        String key,
        String value,
        String description,
        ConfigScope scope,
        boolean featureFlag,
        String tenantId,
        Instant updatedAt
) {}
