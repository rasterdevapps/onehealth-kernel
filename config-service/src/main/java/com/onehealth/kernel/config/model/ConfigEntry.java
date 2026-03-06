package com.onehealth.kernel.config.model;

import com.onehealth.kernel.auth.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "config_entries", schema = "kernel_config")
public class ConfigEntry extends BaseEntity {

    @Column(name = "config_key", nullable = false)
    private String configKey;

    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope", nullable = false)
    private ConfigScope scope = ConfigScope.TENANT;

    @Column(name = "encrypted")
    private boolean encrypted = false;

    @Column(name = "feature_flag")
    private boolean featureFlag = false;

    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }

    public String getConfigValue() { return configValue; }
    public void setConfigValue(String configValue) { this.configValue = configValue; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ConfigScope getScope() { return scope; }
    public void setScope(ConfigScope scope) { this.scope = scope; }

    public boolean isEncrypted() { return encrypted; }
    public void setEncrypted(boolean encrypted) { this.encrypted = encrypted; }

    public boolean isFeatureFlag() { return featureFlag; }
    public void setFeatureFlag(boolean featureFlag) { this.featureFlag = featureFlag; }
}
