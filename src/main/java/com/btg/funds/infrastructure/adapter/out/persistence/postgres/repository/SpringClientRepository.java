package com.btg.funds.infrastructure.adapter.out.persistence.postgres.repository;

import com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity.ClientEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringClientRepository extends JpaRepository<ClientEntity, String> {
    Optional<ClientEntity> findByDocumentNumber(String documentNumber);
}
