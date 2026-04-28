package com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "subscriptions", indexes = {
        @Index(name = "idx_subscription_client", columnList = "clientId"),
        @Index(name = "idx_subscription_fund", columnList = "fundId")
})
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;
    @Column(nullable = false)
    public String clientId;
    @Column(nullable = false)
    public String fundId;
    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal amount;
    @Column(nullable = false)
    public Instant subscribedAt;
    @Column(nullable = false)
    public boolean active;
}
