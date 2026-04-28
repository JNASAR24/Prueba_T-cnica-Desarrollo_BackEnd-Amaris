package com.btg.funds.infrastructure.adapter.out.persistence.postgres.repository;

import com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity.FundEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringFundRepository extends JpaRepository<FundEntity, String> {
}
