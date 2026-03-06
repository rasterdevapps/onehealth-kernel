package com.onehealth.kernel.authorization.repository;

import com.onehealth.kernel.authorization.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    List<Role> findAllByTenantId(String tenantId);
    Optional<Role> findByIdAndTenantId(UUID id, String tenantId);
    boolean existsByNameAndTenantId(String name, String tenantId);
}
