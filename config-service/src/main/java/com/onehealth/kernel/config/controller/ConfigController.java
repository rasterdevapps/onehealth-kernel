package com.onehealth.kernel.config.controller;

import com.onehealth.kernel.api.ApiResponse;
import com.onehealth.kernel.config.dto.ConfigResponse;
import com.onehealth.kernel.config.dto.SetConfigRequest;
import com.onehealth.kernel.config.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/config")
@Tag(name = "Configuration", description = "Dynamic configuration and feature flags")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @PutMapping
    @Operation(summary = "Set or update a configuration entry")
    public ResponseEntity<ApiResponse<ConfigResponse>> setConfig(@Valid @RequestBody SetConfigRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(configService.setConfig(request)));
    }

    @GetMapping
    @Operation(summary = "List all configuration entries for current tenant")
    public ResponseEntity<ApiResponse<List<ConfigResponse>>> listAll() {
        return ResponseEntity.ok(ApiResponse.ok(configService.listAll()));
    }

    @GetMapping("/value/{key}")
    @Operation(summary = "Get a single configuration value by key")
    public ResponseEntity<ApiResponse<String>> getValue(@PathVariable String key) {
        return configService.getValue(key)
                .map(value -> ResponseEntity.ok(ApiResponse.ok(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/feature-flags")
    @Operation(summary = "List all feature flags for current tenant")
    public ResponseEntity<ApiResponse<List<ConfigResponse>>> listFeatureFlags() {
        return ResponseEntity.ok(ApiResponse.ok(configService.listFeatureFlags()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a configuration entry")
    public ResponseEntity<ApiResponse<Void>> deleteConfig(@PathVariable UUID id) {
        configService.deleteConfig(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
