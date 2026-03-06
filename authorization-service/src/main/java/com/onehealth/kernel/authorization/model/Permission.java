package com.onehealth.kernel.authorization.model;

import com.onehealth.kernel.auth.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "permissions", schema = "kernel_authorization")
public class Permission extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "resource", nullable = false)
    private String resource;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "description")
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
