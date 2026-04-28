package com.btg.funds.infrastructure.adapter.out.persistence.postgres.repository;

import com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringSubscriptionRepository extends JpaRepository<SubscriptionEntity, String> {
}
