package com.onehealth.kernel.metadata.domain;

import com.onehealth.kernel.auth.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DdicDomainTest {

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void shouldCreateDdicDomain() {
        DdicDomain domain = new DdicDomain();
        domain.setDomainName("CURRENCY");
        domain.setDataType("VARCHAR");
        domain.setLength(3);
        domain.setDescription("Currency code domain");

        assertEquals("CURRENCY", domain.getDomainName());
        assertEquals("VARCHAR", domain.getDataType());
        assertEquals(3, domain.getLength());
        assertTrue(domain.isActive());
    }

    @Test
    void shouldSetTenantIdOnPrePersist() {
        TenantContext.setTenantId("tenant-test");

        DdicDomain domain = new DdicDomain();
        domain.setDomainName("AMOUNT");
        domain.setDataType("DECIMAL");
        domain.prePersist();

        assertEquals("tenant-test", domain.getTenantId());
    }

    @Test
    void shouldNotOverrideTenantIdIfAlreadySet() {
        TenantContext.setTenantId("tenant-context");

        DdicDomain domain = new DdicDomain();
        domain.setTenantId("tenant-explicit");
        domain.prePersist();

        assertEquals("tenant-explicit", domain.getTenantId());
    }

    @Test
    void shouldDefaultActiveToTrue() {
        DdicDomain domain = new DdicDomain();
        assertTrue(domain.isActive());
    }
}
