package com.onehealth.kernel.config.repository;

import com.onehealth.kernel.config.model.ConfigEntry;
import com.onehealth.kernel.config.model.ConfigScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConfigEntryRepository extends JpaRepository<ConfigEntry, UUID> {
    Optional<ConfigEntry> findByConfigKeyAndTenantId(String key, String tenantId);
    List<ConfigEntry> findAllByTenantId(String tenantId);
    List<ConfigEntry> findAllByTenantIdAndScope(String tenantId, ConfigScope scope);
    List<ConfigEntry> findAllByTenantIdAndFeatureFlagTrue(String tenantId);
    boolean existsByConfigKeyAndTenantId(String key, String tenantId);
}
