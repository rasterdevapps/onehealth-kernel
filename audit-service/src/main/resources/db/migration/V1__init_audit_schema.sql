-- Audit Service Schema Init
CREATE SCHEMA IF NOT EXISTS kernel_audit;

CREATE TABLE IF NOT EXISTS kernel_audit.audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(100) NOT NULL,
    action_type VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100),
    entity_id VARCHAR(255),
    user_id VARCHAR(255),
    username VARCHAR(255),
    ip_address VARCHAR(100),
    user_agent VARCHAR(500),
    details TEXT,
    outcome VARCHAR(50),
    occurred_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Audit logs are immutable; no update or delete is allowed via application
-- Enforce this at the DB level with a rule:
CREATE OR REPLACE RULE no_update_audit_logs AS ON UPDATE TO kernel_audit.audit_logs DO INSTEAD NOTHING;
CREATE OR REPLACE RULE no_delete_audit_logs AS ON DELETE TO kernel_audit.audit_logs DO INSTEAD NOTHING;

CREATE INDEX IF NOT EXISTS idx_audit_tenant_id ON kernel_audit.audit_logs(tenant_id);
CREATE INDEX IF NOT EXISTS idx_audit_action_type ON kernel_audit.audit_logs(action_type);
CREATE INDEX IF NOT EXISTS idx_audit_username ON kernel_audit.audit_logs(username);
CREATE INDEX IF NOT EXISTS idx_audit_occurred_at ON kernel_audit.audit_logs(occurred_at DESC);
CREATE INDEX IF NOT EXISTS idx_audit_entity ON kernel_audit.audit_logs(entity_type, entity_id);
