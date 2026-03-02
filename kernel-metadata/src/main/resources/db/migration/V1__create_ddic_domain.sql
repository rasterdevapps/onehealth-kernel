CREATE TABLE kern_ddic_domain (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    domain_name VARCHAR(255) NOT NULL,
    data_type VARCHAR(255) NOT NULL,
    length INTEGER,
    decimal_places INTEGER,
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

ALTER TABLE kern_ddic_domain ADD CONSTRAINT uq_kern_ddic_domain_tenant_name UNIQUE (tenant_id, domain_name);
CREATE INDEX idx_kern_ddic_domain_tenant_id ON kern_ddic_domain(tenant_id);
CREATE INDEX idx_kern_ddic_domain_domain_name ON kern_ddic_domain(domain_name);
