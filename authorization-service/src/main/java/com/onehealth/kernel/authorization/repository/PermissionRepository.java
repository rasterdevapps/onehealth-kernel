package com.onehealth.kernel.authorization.repository;

import com.onehealth.kernel.authorization.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    List<Permission> findAllByTenantId(String tenantId);
    Optional<Permission> findByIdAndTenantId(UUID id, String tenantId);
    boolean existsByNameAndTenantId(String name, String tenantId);
}
