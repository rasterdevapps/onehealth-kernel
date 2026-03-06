package com.onehealth.kernel.tenant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateTenantRequest(
        @NotBlank @Pattern(regexp = "^[A-Z0-9_]{2,50}$", message = "Code must be uppercase alphanumeric with underscores")
        String code,
        @NotBlank @Size(max = 200) String name,
        String description,
        String industry,
        String countryCode,
        String timezone
) {}
