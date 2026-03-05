package com.onehealth.his.service;

import com.onehealth.erp.common.audit.AuditLogger;
import com.onehealth.erp.common.dto.PatientDTO;
import com.onehealth.his.domain.Patient;
import com.onehealth.his.repository.PatientRepository;
import com.onehealth.kernel.auth.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AuditLogger auditLogger;

    public PatientService(PatientRepository patientRepository, AuditLogger auditLogger) {
        this.patientRepository = patientRepository;
        this.auditLogger = auditLogger;
    }

    public PatientDTO registerPatient(PatientDTO dto) {
        validateFields(dto);

        Patient patient = new Patient();
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setNationalId(dto.getNationalId());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setEmail(dto.getEmail());

        Patient saved = patientRepository.save(patient);

        auditLogger.log("PATIENT_REGISTERED", "Patient", saved.getId().toString(), "system");

        return toDto(saved);
    }

    public PatientDTO getPatient(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + id));
        return toDto(patient);
    }

    public List<PatientDTO> getPatientsByTenant() {
        String tenantId = TenantContext.getTenantId();
        return patientRepository.findByTenantId(tenantId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private void validateFields(PatientDTO dto) {
        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            throw new IllegalArgumentException("DDIC Validation: firstName is required (DataElement: PATIENT_FIRST_NAME)");
        }
        if (dto.getLastName() == null || dto.getLastName().isBlank()) {
            throw new IllegalArgumentException("DDIC Validation: lastName is required (DataElement: PATIENT_LAST_NAME)");
        }
    }

    private PatientDTO toDto(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setTenantId(patient.getTenantId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setGender(patient.getGender());
        dto.setNationalId(patient.getNationalId());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setEmail(patient.getEmail());
        return dto;
    }
}
