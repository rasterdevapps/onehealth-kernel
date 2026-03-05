package com.onehealth.emr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {
    "com.onehealth.emr",
    "com.onehealth.kernel.auth",
    "com.onehealth.erp.common"
})
public class EmrApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmrApplication.class, args);
    }
}
