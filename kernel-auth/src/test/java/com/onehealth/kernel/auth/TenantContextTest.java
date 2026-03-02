package com.onehealth.kernel.auth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TenantContextTest {

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void shouldSetAndGetTenantId() {
        TenantContext.setTenantId("tenant-1");
        assertEquals("tenant-1", TenantContext.getTenantId());
    }

    @Test
    void shouldClearTenantId() {
        TenantContext.setTenantId("tenant-1");
        TenantContext.clear();
        assertNull(TenantContext.getTenantId());
    }

    @Test
    void shouldReturnNullWhenNotSet() {
        assertNull(TenantContext.getTenantId());
    }

    @Test
    void shouldInheritTenantIdInChildThread() throws Exception {
        TenantContext.setTenantId("tenant-inherited");

        Thread childThread = new Thread(() -> {
            assertEquals("tenant-inherited", TenantContext.getTenantId());
        });
        childThread.start();
        childThread.join();
    }
}
