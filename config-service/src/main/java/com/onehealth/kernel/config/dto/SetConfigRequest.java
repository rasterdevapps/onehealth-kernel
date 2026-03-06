package com.onehealth.kernel.config.dto;

import com.onehealth.kernel.config.model.ConfigScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SetConfigRequest(
        @NotBlank String key,
        String value,
        String description,
        @NotNull ConfigScope scope,
        boolean featureFlag
) {}
