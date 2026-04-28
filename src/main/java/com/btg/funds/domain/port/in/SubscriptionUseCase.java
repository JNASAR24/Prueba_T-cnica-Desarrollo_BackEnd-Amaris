package com.btg.funds.domain.port.in;

import com.btg.funds.domain.model.NotificationChannel;
import com.btg.funds.domain.model.Subscription;

public interface SubscriptionUseCase {
    Subscription subscribe(String clientId, String fundId, NotificationChannel channel);
    void cancel(String clientId, String subscriptionId, NotificationChannel channel);
}
