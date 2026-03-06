package com.onehealth.kernel.audit.model;

/**
 * Standard audit action types for the ERP Kernel.
 */
public final class AuditActionType {

    // Identity actions
    public static final String USER_LOGIN = "USER_LOGIN";
    public static final String USER_LOGOUT = "USER_LOGOUT";
    public static final String USER_LOGIN_FAILED = "USER_LOGIN_FAILED";
    public static final String USER_CREATED = "USER_CREATED";
    public static final String USER_UPDATED = "USER_UPDATED";
    public static final String USER_DELETED = "USER_DELETED";
    public static final String PASSWORD_CHANGED = "PASSWORD_CHANGED";
    public static final String ACCOUNT_LOCKED = "ACCOUNT_LOCKED";

    // Authorization actions
    public static final String ROLE_CREATED = "ROLE_CREATED";
    public static final String ROLE_DELETED = "ROLE_DELETED";
    public static final String PERMISSION_CREATED = "PERMISSION_CREATED";
    public static final String PERMISSION_GRANTED = "PERMISSION_GRANTED";
    public static final String PERMISSION_REVOKED = "PERMISSION_REVOKED";
    public static final String ROLE_ASSIGNED = "ROLE_ASSIGNED";
    public static final String ROLE_REVOKED = "ROLE_REVOKED";

    // Tenant actions
    public static final String TENANT_CREATED = "TENANT_CREATED";
    public static final String TENANT_SUSPENDED = "TENANT_SUSPENDED";
    public static final String TENANT_ACTIVATED = "TENANT_ACTIVATED";

    // Config actions
    public static final String CONFIG_UPDATED = "CONFIG_UPDATED";
    public static final String CONFIG_DELETED = "CONFIG_DELETED";

    private AuditActionType() {}
}
