package com.onehealth.kernel.tenant.service;

import com.onehealth.kernel.tenant.dto.CreateTenantRequest;
import com.onehealth.kernel.tenant.dto.TenantResponse;
import com.onehealth.kernel.tenant.model.Tenant;
import com.onehealth.kernel.tenant.model.TenantStatus;
import com.onehealth.kernel.tenant.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public TenantResponse createTenant(CreateTenantRequest request) {
        if (tenantRepository.existsByCode(request.code())) {
            throw new IllegalArgumentException("Tenant with code '" + request.code() + "' already exists");
        }
        Tenant tenant = new Tenant();
        tenant.setCode(request.code());
        tenant.setName(request.name());
        tenant.setDescription(request.description());
        tenant.setIndustry(request.industry());
        tenant.setCountryCode(request.countryCode());
        tenant.setTimezone(request.timezone());
        tenant.setStatus(TenantStatus.ACTIVE);
        tenant = tenantRepository.save(tenant);
        return toResponse(tenant);
    }

    @Transactional(readOnly = true)
    public List<TenantResponse> listTenants() {
        return tenantRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TenantResponse getTenant(UUID id) {
        return tenantRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));
    }

    @Transactional(readOnly = true)
    public TenantResponse getTenantByCode(String code) {
        return tenantRepository.findByCode(code)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));
    }

    public TenantResponse suspendTenant(UUID id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));
        tenant.setStatus(TenantStatus.SUSPENDED);
        return toResponse(tenantRepository.save(tenant));
    }

    public TenantResponse activateTenant(UUID id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));
        tenant.setStatus(TenantStatus.ACTIVE);
        return toResponse(tenantRepository.save(tenant));
    }

    private TenantResponse toResponse(Tenant tenant) {
        return new TenantResponse(
                tenant.getId(), tenant.getCode(), tenant.getName(),
                tenant.getDescription(), tenant.getStatus(),
                tenant.getIndustry(), tenant.getCountryCode(),
                tenant.getTimezone(), tenant.getCreatedAt()
        );
    }
}
