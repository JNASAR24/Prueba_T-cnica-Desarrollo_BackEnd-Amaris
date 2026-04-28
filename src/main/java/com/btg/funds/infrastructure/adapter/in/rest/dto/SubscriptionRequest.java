package com.btg.funds.infrastructure.adapter.in.rest.dto;

import com.btg.funds.domain.model.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubscriptionRequest(
        @NotBlank String fundId,
        @NotNull NotificationChannel notificationChannel
) {
}
