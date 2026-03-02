CREATE TABLE kern_ddic_table_field (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    field_name VARCHAR(255) NOT NULL,
    table_id UUID REFERENCES kern_ddic_table(id),
    data_element_id UUID REFERENCES kern_ddic_data_element(id),
    position INTEGER,
    is_primary_key BOOLEAN DEFAULT FALSE,
    is_nullable BOOLEAN DEFAULT TRUE,
    description TEXT
);

CREATE INDEX idx_kern_ddic_table_field_tenant_id ON kern_ddic_table_field(tenant_id);
CREATE INDEX idx_kern_ddic_table_field_table_id ON kern_ddic_table_field(table_id);
