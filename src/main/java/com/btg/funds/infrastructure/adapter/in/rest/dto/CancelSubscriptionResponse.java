package com.btg.funds.infrastructure.adapter.in.rest.dto;

public record CancelSubscriptionResponse(
        String subscriptionId,
        String message
) {
}
