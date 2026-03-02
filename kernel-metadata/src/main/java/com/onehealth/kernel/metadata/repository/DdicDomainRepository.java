package com.onehealth.kernel.metadata.repository;

import com.onehealth.kernel.metadata.domain.DdicDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DdicDomainRepository extends JpaRepository<DdicDomain, UUID> {

    Optional<DdicDomain> findByDomainName(String domainName);

    List<DdicDomain> findByTenantId(String tenantId);

    List<DdicDomain> findByTenantIdAndActive(String tenantId, boolean active);
}
