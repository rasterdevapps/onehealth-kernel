package com.onehealth.emr.repository;

import com.onehealth.emr.domain.Encounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EncounterRepository extends JpaRepository<Encounter, UUID> {

    List<Encounter> findByTenantId(String tenantId);

    List<Encounter> findByPatientId(UUID patientId);
}
