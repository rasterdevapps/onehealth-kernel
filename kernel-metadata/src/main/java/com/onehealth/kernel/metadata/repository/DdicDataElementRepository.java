package com.onehealth.kernel.metadata.repository;

import com.onehealth.kernel.metadata.domain.DdicDataElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DdicDataElementRepository extends JpaRepository<DdicDataElement, UUID> {

    Optional<DdicDataElement> findByElementName(String elementName);

    List<DdicDataElement> findByTenantId(String tenantId);
}
