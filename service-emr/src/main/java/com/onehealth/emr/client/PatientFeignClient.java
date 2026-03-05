package com.onehealth.emr.client;

import com.onehealth.erp.common.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "service-his", url = "${gateway.url}")
public interface PatientFeignClient {

    @GetMapping("/api/patients/{id}")
    PatientDTO getPatient(@PathVariable("id") UUID id,
                          @RequestHeader("X-Tenant-ID") String tenantId);

    @GetMapping("/api/patients")
    List<PatientDTO> getPatientsByTenant(@RequestHeader("X-Tenant-ID") String tenantId);
}
