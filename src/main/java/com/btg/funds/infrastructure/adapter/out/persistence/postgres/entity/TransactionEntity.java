package com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_tx_client_created", columnList = "clientId,createdAt")
})
public class TransactionEntity {
    @Id
    public String id;
    @Column(nullable = false)
    public String clientId;
    @Column(nullable = false)
    public String subscriptionId;
    @Column(nullable = false)
    public String fundId;
    @Column(nullable = false)
    public String type;
    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal amount;
    @Column(nullable = false)
    public Instant createdAt;
}
