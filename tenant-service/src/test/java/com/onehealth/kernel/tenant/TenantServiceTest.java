package com.onehealth.kernel.tenant;

import com.onehealth.kernel.tenant.dto.CreateTenantRequest;
import com.onehealth.kernel.tenant.dto.TenantResponse;
import com.onehealth.kernel.tenant.model.Tenant;
import com.onehealth.kernel.tenant.model.TenantStatus;
import com.onehealth.kernel.tenant.repository.TenantRepository;
import com.onehealth.kernel.tenant.service.TenantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantServiceTest {

    @Mock
    TenantRepository tenantRepository;

    @InjectMocks
    TenantService tenantService;

    @Test
    void createTenant_shouldSucceed_whenCodeIsUnique() {
        CreateTenantRequest request = new CreateTenantRequest(
                "HOSPITAL_001", "City Hospital", "Main hospital", "HEALTHCARE", "US", "America/New_York");

        when(tenantRepository.existsByCode("HOSPITAL_001")).thenReturn(false);

        Tenant saved = new Tenant();
        saved.setCode("HOSPITAL_001");
        saved.setName("City Hospital");
        saved.setStatus(TenantStatus.ACTIVE);
        when(tenantRepository.save(any(Tenant.class))).thenReturn(saved);

        TenantResponse response = tenantService.createTenant(request);

        assertThat(response.code()).isEqualTo("HOSPITAL_001");
        assertThat(response.name()).isEqualTo("City Hospital");
        assertThat(response.status()).isEqualTo(TenantStatus.ACTIVE);
    }

    @Test
    void createTenant_shouldThrow_whenCodeAlreadyExists() {
        CreateTenantRequest request = new CreateTenantRequest(
                "HOSPITAL_001", "City Hospital", null, null, null, null);

        when(tenantRepository.existsByCode("HOSPITAL_001")).thenReturn(true);

        assertThatThrownBy(() -> tenantService.createTenant(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void suspendTenant_shouldChangStatusToSuspended() {
        UUID id = UUID.randomUUID();
        Tenant tenant = new Tenant();
        tenant.setCode("T001");
        tenant.setName("Test");
        tenant.setStatus(TenantStatus.ACTIVE);

        when(tenantRepository.findById(id)).thenReturn(Optional.of(tenant));
        when(tenantRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        TenantResponse response = tenantService.suspendTenant(id);

        assertThat(response.status()).isEqualTo(TenantStatus.SUSPENDED);
    }

    @Test
    void listTenants_shouldReturnAll() {
        Tenant t1 = new Tenant();
        t1.setCode("T001");
        t1.setName("Tenant 1");
        t1.setStatus(TenantStatus.ACTIVE);
        Tenant t2 = new Tenant();
        t2.setCode("T002");
        t2.setName("Tenant 2");
        t2.setStatus(TenantStatus.SUSPENDED);

        when(tenantRepository.findAll()).thenReturn(List.of(t1, t2));

        List<TenantResponse> result = tenantService.listTenants();

        assertThat(result).hasSize(2);
    }
}
