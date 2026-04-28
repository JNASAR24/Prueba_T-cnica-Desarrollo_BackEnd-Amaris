package com.btg.funds.infrastructure.adapter.out.persistence.postgres.repository;

import com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity.TransactionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringTransactionRepository extends JpaRepository<TransactionEntity, String> {
    List<TransactionEntity> findByClientIdOrderByCreatedAtDesc(String clientId);
}
