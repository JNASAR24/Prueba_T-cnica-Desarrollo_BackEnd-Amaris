package com.btg.funds.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Transaction(
        String id,
        String clientId,
        String subscriptionId,
        String fundId,
        TransactionType type,
        BigDecimal amount,
        Instant createdAt
) {
}
