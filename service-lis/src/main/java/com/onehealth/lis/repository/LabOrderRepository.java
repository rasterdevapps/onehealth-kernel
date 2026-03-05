package com.onehealth.lis.repository;

import com.onehealth.lis.domain.LabOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, UUID> {

    List<LabOrder> findByTenantId(String tenantId);

    List<LabOrder> findByPatientId(UUID patientId);
}
