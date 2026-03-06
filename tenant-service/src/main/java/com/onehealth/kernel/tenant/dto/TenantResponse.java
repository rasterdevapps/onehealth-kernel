package com.onehealth.kernel.tenant.dto;

import com.onehealth.kernel.tenant.model.TenantStatus;

import java.time.Instant;
import java.util.UUID;

public record TenantResponse(
        UUID id,
        String code,
        String name,
        String description,
        TenantStatus status,
        String industry,
        String countryCode,
        String timezone,
        Instant createdAt
) {}
