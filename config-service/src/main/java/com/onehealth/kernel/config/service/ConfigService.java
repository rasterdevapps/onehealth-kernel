package com.onehealth.kernel.config.service;

import com.onehealth.kernel.auth.TenantContext;
import com.onehealth.kernel.config.dto.ConfigResponse;
import com.onehealth.kernel.config.dto.SetConfigRequest;
import com.onehealth.kernel.config.model.ConfigEntry;
import com.onehealth.kernel.config.repository.ConfigEntryRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ConfigService {

    private static final String CACHE_NAME = "config";

    private final ConfigEntryRepository configEntryRepository;

    public ConfigService(ConfigEntryRepository configEntryRepository) {
        this.configEntryRepository = configEntryRepository;
    }

    @CacheEvict(value = CACHE_NAME, key = "#request.key() + ':' + T(com.onehealth.kernel.auth.TenantContext).getTenantId()")
    public ConfigResponse setConfig(SetConfigRequest request) {
        String tenantId = TenantContext.getTenantId();
        ConfigEntry entry = configEntryRepository
                .findByConfigKeyAndTenantId(request.key(), tenantId)
                .orElse(new ConfigEntry());

        entry.setConfigKey(request.key());
        entry.setConfigValue(request.value());
        entry.setDescription(request.description());
        entry.setScope(request.scope());
        entry.setFeatureFlag(request.featureFlag());
        entry = configEntryRepository.save(entry);
        return toResponse(entry);
    }

    @Cacheable(value = CACHE_NAME, key = "#key + ':' + T(com.onehealth.kernel.auth.TenantContext).getTenantId()")
    @Transactional(readOnly = true)
    public Optional<String> getValue(String key) {
        String tenantId = TenantContext.getTenantId();
        return configEntryRepository.findByConfigKeyAndTenantId(key, tenantId)
                .map(ConfigEntry::getConfigValue);
    }

    @Transactional(readOnly = true)
    public List<ConfigResponse> listAll() {
        String tenantId = TenantContext.getTenantId();
        return configEntryRepository.findAllByTenantId(tenantId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConfigResponse> listFeatureFlags() {
        String tenantId = TenantContext.getTenantId();
        return configEntryRepository.findAllByTenantIdAndFeatureFlagTrue(tenantId).stream()
                .map(this::toResponse)
                .toList();
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void deleteConfig(UUID id) {
        configEntryRepository.deleteById(id);
    }

    private ConfigResponse toResponse(ConfigEntry entry) {
        return new ConfigResponse(
                entry.getId(), entry.getConfigKey(), entry.getConfigValue(),
                entry.getDescription(), entry.getScope(), entry.isFeatureFlag(),
                entry.getTenantId(), entry.getUpdatedAt()
        );
    }
}
