package com.onehealth.kernel.tenant.controller;

import com.onehealth.kernel.api.ApiResponse;
import com.onehealth.kernel.tenant.dto.CreateTenantRequest;
import com.onehealth.kernel.tenant.dto.TenantResponse;
import com.onehealth.kernel.tenant.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tenants")
@Tag(name = "Tenant Management", description = "Tenant lifecycle management")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping
    @Operation(summary = "Create a new tenant")
    public ResponseEntity<ApiResponse<TenantResponse>> createTenant(@Valid @RequestBody CreateTenantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(tenantService.createTenant(request)));
    }

    @GetMapping
    @Operation(summary = "List all tenants")
    public ResponseEntity<ApiResponse<List<TenantResponse>>> listTenants() {
        return ResponseEntity.ok(ApiResponse.ok(tenantService.listTenants()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tenant by ID")
    public ResponseEntity<ApiResponse<TenantResponse>> getTenant(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(tenantService.getTenant(id)));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get tenant by code")
    public ResponseEntity<ApiResponse<TenantResponse>> getTenantByCode(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.ok(tenantService.getTenantByCode(code)));
    }

    @PostMapping("/{id}/suspend")
    @Operation(summary = "Suspend a tenant")
    public ResponseEntity<ApiResponse<TenantResponse>> suspendTenant(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(tenantService.suspendTenant(id)));
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate a tenant")
    public ResponseEntity<ApiResponse<TenantResponse>> activateTenant(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(tenantService.activateTenant(id)));
    }
}
