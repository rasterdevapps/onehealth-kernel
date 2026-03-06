package com.onehealth.kernel.authorization.controller;

import com.onehealth.kernel.api.ApiResponse;
import com.onehealth.kernel.authorization.dto.*;
import com.onehealth.kernel.authorization.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Authorization", description = "Role and permission management")
public class RoleController {

    private final AuthorizationService authorizationService;

    public RoleController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping
    @Operation(summary = "Create a new role")
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody CreateRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(authorizationService.createRole(request)));
    }

    @GetMapping
    @Operation(summary = "List all roles for current tenant")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> listRoles() {
        return ResponseEntity.ok(ApiResponse.ok(authorizationService.listRoles()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get role by ID")
    public ResponseEntity<ApiResponse<RoleResponse>> getRole(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(authorizationService.getRole(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a role")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable UUID id) {
        authorizationService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    @Operation(summary = "Grant a permission to a role")
    public ResponseEntity<ApiResponse<Void>> grantPermission(@PathVariable UUID roleId, @PathVariable UUID permissionId) {
        authorizationService.grantPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @Operation(summary = "Revoke a permission from a role")
    public ResponseEntity<ApiResponse<Void>> revokePermission(@PathVariable UUID roleId, @PathVariable UUID permissionId) {
        authorizationService.revokePermissionFromRole(roleId, permissionId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
