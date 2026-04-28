package com.btg.funds.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Subscription(
        String id,
        String clientId,
        String fundId,
        BigDecimal amount,
        Instant subscribedAt,
        boolean active
) {
}
