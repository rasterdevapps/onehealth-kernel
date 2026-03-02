CREATE TABLE kern_ddic_data_element (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    element_name VARCHAR(255) NOT NULL,
    domain_id UUID REFERENCES kern_ddic_domain(id),
    field_label VARCHAR(255),
    description TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

ALTER TABLE kern_ddic_data_element ADD CONSTRAINT uq_kern_ddic_data_element_tenant_name UNIQUE (tenant_id, element_name);
CREATE INDEX idx_kern_ddic_data_element_tenant_id ON kern_ddic_data_element(tenant_id);
CREATE INDEX idx_kern_ddic_data_element_element_name ON kern_ddic_data_element(element_name);
