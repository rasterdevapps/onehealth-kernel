CREATE TABLE kern_ddic_table (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    table_name VARCHAR(255) NOT NULL,
    description TEXT,
    table_category VARCHAR(50),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

ALTER TABLE kern_ddic_table ADD CONSTRAINT uq_kern_ddic_table_tenant_name UNIQUE (tenant_id, table_name);
CREATE INDEX idx_kern_ddic_table_tenant_id ON kern_ddic_table(tenant_id);
CREATE INDEX idx_kern_ddic_table_table_name ON kern_ddic_table(table_name);
