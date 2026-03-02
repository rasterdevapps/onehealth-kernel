package com.onehealth.kernel.metadata.domain;

import com.onehealth.kernel.auth.TenantAware;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "kern_ddic_table_field")
public class DdicTableField extends TenantAware {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private DdicTable table;

    @ManyToOne
    @JoinColumn(name = "data_element_id")
    private DdicDataElement dataElement;

    @Column(name = "position")
    private Integer position;

    @Column(name = "is_primary_key")
    private boolean isPrimaryKey;

    @Column(name = "is_nullable")
    private boolean isNullable;

    @Column(name = "description")
    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public DdicTable getTable() {
        return table;
    }

    public void setTable(DdicTable table) {
        this.table = table;
    }

    public DdicDataElement getDataElement() {
        return dataElement;
    }

    public void setDataElement(DdicDataElement dataElement) {
        this.dataElement = dataElement;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean nullable) {
        isNullable = nullable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
