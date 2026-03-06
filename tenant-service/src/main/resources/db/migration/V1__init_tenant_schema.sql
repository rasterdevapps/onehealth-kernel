-- Tenant Service Schema Init
CREATE SCHEMA IF NOT EXISTS kernel_tenant;

CREATE TABLE IF NOT EXISTS kernel_tenant.tenants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    industry VARCHAR(100),
    country_code VARCHAR(10),
    timezone VARCHAR(100),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_tenants_code ON kernel_tenant.tenants(code);
CREATE INDEX IF NOT EXISTS idx_tenants_status ON kernel_tenant.tenants(status);
