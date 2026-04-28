package com.btg.funds.domain.model;

import java.math.BigDecimal;
import java.util.Set;

public record Client(
        String id,
        String documentNumber,
        String fullName,
        String email,
        String phone,
        String passwordHash,
        Set<Role> roles,
        BigDecimal availableBalance
) {
}
