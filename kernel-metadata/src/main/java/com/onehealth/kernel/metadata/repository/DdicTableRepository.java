package com.onehealth.kernel.metadata.repository;

import com.onehealth.kernel.metadata.domain.DdicTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DdicTableRepository extends JpaRepository<DdicTable, UUID> {

    Optional<DdicTable> findByTenantIdAndTableName(String tenantId, String tableName);

    List<DdicTable> findByTenantId(String tenantId);
}
