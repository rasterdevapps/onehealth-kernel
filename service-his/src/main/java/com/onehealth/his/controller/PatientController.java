package com.onehealth.his.controller;

import com.onehealth.erp.common.dto.PatientDTO;
import com.onehealth.his.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<PatientDTO> register(@RequestBody PatientDTO dto) {
        PatientDTO created = patientService.registerPatient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.getPatient(id));
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> listByTenant() {
        return ResponseEntity.ok(patientService.getPatientsByTenant());
    }
}
