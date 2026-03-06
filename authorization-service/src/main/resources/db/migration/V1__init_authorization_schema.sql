-- Authorization Service Schema Init
CREATE SCHEMA IF NOT EXISTS kernel_authorization;

CREATE TABLE IF NOT EXISTS kernel_authorization.roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    system_role BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMPTZ,
    CONSTRAINT uq_roles_name_tenant UNIQUE (name, tenant_id)
);

CREATE TABLE IF NOT EXISTS kernel_authorization.permissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    resource VARCHAR(100) NOT NULL,
    action VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMPTZ,
    CONSTRAINT uq_permissions_name_tenant UNIQUE (name, tenant_id)
);

CREATE TABLE IF NOT EXISTS kernel_authorization.role_permissions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMPTZ,
    CONSTRAINT uq_role_permissions UNIQUE (role_id, permission_id),
    CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_id) REFERENCES kernel_authorization.roles(id),
    CONSTRAINT fk_role_permissions_permission FOREIGN KEY (permission_id) REFERENCES kernel_authorization.permissions(id)
);

CREATE TABLE IF NOT EXISTS kernel_authorization.user_roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMPTZ,
    CONSTRAINT uq_user_roles UNIQUE (user_id, role_id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES kernel_authorization.roles(id)
);

CREATE INDEX IF NOT EXISTS idx_roles_tenant_id ON kernel_authorization.roles(tenant_id);
CREATE INDEX IF NOT EXISTS idx_permissions_tenant_id ON kernel_authorization.permissions(tenant_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON kernel_authorization.user_roles(user_id);
CREATE INDEX IF NOT EXISTS idx_role_permissions_role_id ON kernel_authorization.role_permissions(role_id);
