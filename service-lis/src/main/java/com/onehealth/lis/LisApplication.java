package com.onehealth.lis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.onehealth.lis",
    "com.onehealth.kernel.auth",
    "com.onehealth.erp.common"
})
public class LisApplication {

    public static void main(String[] args) {
        SpringApplication.run(LisApplication.class, args);
    }
}
