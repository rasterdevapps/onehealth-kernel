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

@RestController
@RequestMapping("/api/permissions")
@Tag(name = "Authorization", description = "Permission management")
public class PermissionController {

    private final AuthorizationService authorizationService;

    public PermissionController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping
    @Operation(summary = "Create a new permission")
    public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(
            @Valid @RequestBody CreatePermissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(authorizationService.createPermission(request)));
    }

    @GetMapping
    @Operation(summary = "List all permissions for current tenant")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> listPermissions() {
        return ResponseEntity.ok(ApiResponse.ok(authorizationService.listPermissions()));
    }
}
