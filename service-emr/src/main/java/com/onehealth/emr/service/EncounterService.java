package com.onehealth.emr.service;

import com.onehealth.emr.client.PatientFeignClient;
import com.onehealth.emr.domain.Encounter;
import com.onehealth.emr.repository.EncounterRepository;
import com.onehealth.erp.common.audit.AuditLogger;
import com.onehealth.erp.common.dto.EncounterDTO;
import com.onehealth.erp.common.dto.PatientDTO;
import com.onehealth.kernel.auth.TenantContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EncounterService {

    private final EncounterRepository encounterRepository;
    private final PatientFeignClient patientFeignClient;
    private final AuditLogger auditLogger;

    public EncounterService(EncounterRepository encounterRepository,
                            PatientFeignClient patientFeignClient,
                            AuditLogger auditLogger) {
        this.encounterRepository = encounterRepository;
        this.patientFeignClient = patientFeignClient;
        this.auditLogger = auditLogger;
    }

    public EncounterDTO createEncounter(EncounterDTO dto) {
        String tenantId = TenantContext.getTenantId();

        PatientDTO patient = patientFeignClient.getPatient(dto.getPatientId(), tenantId);

        Encounter encounter = new Encounter();
        encounter.setPatientId(dto.getPatientId());
        encounter.setPatientName(patient.getFirstName() + " " + patient.getLastName());
        encounter.setEncounterType(dto.getEncounterType());
        encounter.setDiagnosis(dto.getDiagnosis());
        encounter.setNotes(dto.getNotes());
        encounter.setStatus("OPEN");
        encounter.setEncounterDate(Instant.now());

        Encounter saved = encounterRepository.save(encounter);

        auditLogger.log("ENCOUNTER_CREATED", "Encounter", saved.getId().toString(), "system");

        return toDto(saved);
    }

    public List<EncounterDTO> getEncountersByTenant() {
        String tenantId = TenantContext.getTenantId();
        return encounterRepository.findByTenantId(tenantId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public EncounterDTO updateStatus(UUID id, String status) {
        Encounter encounter = encounterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encounter not found: " + id));
        encounter.setStatus(status);
        Encounter saved = encounterRepository.save(encounter);

        auditLogger.log("ENCOUNTER_STATUS_UPDATED", "Encounter", saved.getId().toString(), "system");

        return toDto(saved);
    }

    private EncounterDTO toDto(Encounter encounter) {
        EncounterDTO dto = new EncounterDTO();
        dto.setId(encounter.getId());
        dto.setTenantId(encounter.getTenantId());
        dto.setPatientId(encounter.getPatientId());
        dto.setPatientName(encounter.getPatientName());
        dto.setEncounterType(encounter.getEncounterType());
        dto.setDiagnosis(encounter.getDiagnosis());
        dto.setNotes(encounter.getNotes());
        dto.setStatus(encounter.getStatus());
        dto.setEncounterDate(encounter.getEncounterDate());
        return dto;
    }
}
