package com.onehealth.kernel.auth;

public final class TenantContext {

    private static final InheritableThreadLocal<String> CURRENT_TENANT = new InheritableThreadLocal<>();

    private TenantContext() {
    }

    public static void setTenantId(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static String getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
