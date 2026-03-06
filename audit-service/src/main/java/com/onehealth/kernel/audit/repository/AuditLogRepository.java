package com.onehealth.kernel.audit.repository;

import com.onehealth.kernel.audit.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    Page<AuditLog> findAllByTenantId(String tenantId, Pageable pageable);

    Page<AuditLog> findAllByTenantIdAndActionType(String tenantId, String actionType, Pageable pageable);

    Page<AuditLog> findAllByTenantIdAndUsername(String tenantId, String username, Pageable pageable);

    @Query("SELECT a FROM AuditLog a WHERE a.tenantId = :tenantId AND a.occurredAt BETWEEN :from AND :to ORDER BY a.occurredAt DESC")
    Page<AuditLog> findByTenantIdAndDateRange(
            @Param("tenantId") String tenantId,
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable);
}
