package com.onehealth.kernel.metadata;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.onehealth.kernel.metadata.domain")
@EnableJpaRepositories(basePackages = "com.onehealth.kernel.metadata.repository")
public class MetadataAutoConfiguration {
}
