package com.onehealth.kernel.audit.controller;

import com.onehealth.kernel.api.ApiResponse;
import com.onehealth.kernel.audit.dto.AuditLogResponse;
import com.onehealth.kernel.audit.dto.RecordAuditRequest;
import com.onehealth.kernel.audit.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/audit")
@Tag(name = "Audit", description = "Immutable audit log recording and querying")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping
    @Operation(summary = "Record an audit event")
    public ResponseEntity<ApiResponse<AuditLogResponse>> record(@Valid @RequestBody RecordAuditRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(auditService.record(request)));
    }

    @GetMapping
    @Operation(summary = "Query audit logs with pagination")
    public ResponseEntity<ApiResponse<Page<AuditLogResponse>>> listAll(
            @PageableDefault(size = 50, sort = "occurredAt") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(auditService.listAll(pageable)));
    }

    @GetMapping("/action/{actionType}")
    @Operation(summary = "Query audit logs by action type")
    public ResponseEntity<ApiResponse<Page<AuditLogResponse>>> listByActionType(
            @PathVariable String actionType,
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(auditService.listByActionType(actionType, pageable)));
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Query audit logs by username")
    public ResponseEntity<ApiResponse<Page<AuditLogResponse>>> listByUsername(
            @PathVariable String username,
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(auditService.listByUsername(username, pageable)));
    }

    @GetMapping("/range")
    @Operation(summary = "Query audit logs within a date range")
    public ResponseEntity<ApiResponse<Page<AuditLogResponse>>> listByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(auditService.listByDateRange(from, to, pageable)));
    }
}
