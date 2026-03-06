package com.onehealth.kernel.audit.service;

import com.onehealth.kernel.audit.dto.AuditLogResponse;
import com.onehealth.kernel.audit.dto.RecordAuditRequest;
import com.onehealth.kernel.audit.model.AuditLog;
import com.onehealth.kernel.audit.repository.AuditLogRepository;
import com.onehealth.kernel.auth.TenantContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public AuditLogResponse record(RecordAuditRequest request) {
        String tenantId = TenantContext.getTenantId();
        AuditLog log = new AuditLog();
        log.setTenantId(tenantId);
        log.setActionType(request.actionType());
        log.setEntityType(request.entityType());
        log.setEntityId(request.entityId());
        log.setUserId(request.userId());
        log.setUsername(request.username());
        log.setIpAddress(request.ipAddress());
        log.setUserAgent(request.userAgent());
        log.setDetails(request.details());
        log.setOutcome(request.outcome());
        log = auditLogRepository.save(log);
        return toResponse(log);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogResponse> listAll(Pageable pageable) {
        String tenantId = TenantContext.getTenantId();
        return auditLogRepository.findAllByTenantId(tenantId, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogResponse> listByActionType(String actionType, Pageable pageable) {
        String tenantId = TenantContext.getTenantId();
        return auditLogRepository.findAllByTenantIdAndActionType(tenantId, actionType, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogResponse> listByUsername(String username, Pageable pageable) {
        String tenantId = TenantContext.getTenantId();
        return auditLogRepository.findAllByTenantIdAndUsername(tenantId, username, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogResponse> listByDateRange(Instant from, Instant to, Pageable pageable) {
        String tenantId = TenantContext.getTenantId();
        return auditLogRepository.findByTenantIdAndDateRange(tenantId, from, to, pageable)
                .map(this::toResponse);
    }

    private AuditLogResponse toResponse(AuditLog log) {
        return new AuditLogResponse(
                log.getId(), log.getTenantId(), log.getActionType(),
                log.getEntityType(), log.getEntityId(),
                log.getUserId(), log.getUsername(),
                log.getIpAddress(), log.getUserAgent(),
                log.getDetails(), log.getOutcome(), log.getOccurredAt()
        );
    }
}
