package com.btg.funds.domain.port.out;

import com.btg.funds.domain.model.Subscription;
import java.util.Optional;

public interface SubscriptionRepositoryPort {
    Subscription save(Subscription subscription);
    Optional<Subscription> findById(String id);
}
