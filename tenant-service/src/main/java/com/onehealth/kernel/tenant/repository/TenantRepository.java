package com.onehealth.kernel.tenant.repository;

import com.onehealth.kernel.tenant.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    Optional<Tenant> findByCode(String code);
    boolean existsByCode(String code);
}
