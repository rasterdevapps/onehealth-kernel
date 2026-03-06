package com.onehealth.kernel.identity.dto;

import com.onehealth.kernel.identity.model.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        UserStatus status,
        String tenantId,
        Instant createdAt
) {}
