-- Config Service Schema Init
CREATE SCHEMA IF NOT EXISTS kernel_config;

CREATE TABLE IF NOT EXISTS kernel_config.config_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    config_key VARCHAR(255) NOT NULL,
    config_value TEXT,
    description VARCHAR(500),
    scope VARCHAR(50) NOT NULL DEFAULT 'TENANT',
    encrypted BOOLEAN NOT NULL DEFAULT FALSE,
    feature_flag BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted_at TIMESTAMPTZ,
    CONSTRAINT uq_config_key_tenant UNIQUE (config_key, tenant_id)
);

CREATE INDEX IF NOT EXISTS idx_config_tenant_id ON kernel_config.config_entries(tenant_id);
CREATE INDEX IF NOT EXISTS idx_config_key ON kernel_config.config_entries(config_key);
CREATE INDEX IF NOT EXISTS idx_config_feature_flags ON kernel_config.config_entries(tenant_id, feature_flag);
