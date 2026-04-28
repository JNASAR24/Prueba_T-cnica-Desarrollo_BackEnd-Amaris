package com.btg.funds.infrastructure.adapter.out.persistence.postgres;

import com.btg.funds.domain.model.Subscription;
import com.btg.funds.domain.port.out.SubscriptionRepositoryPort;
import com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity.SubscriptionEntity;
import com.btg.funds.infrastructure.adapter.out.persistence.postgres.repository.SpringSubscriptionRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionRepositoryAdapter implements SubscriptionRepositoryPort {

    private final SpringSubscriptionRepository repository;

    public SubscriptionRepositoryAdapter(SpringSubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        return toDomain(repository.save(toEntity(subscription)));
    }

    @Override
    public Optional<Subscription> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    private Subscription toDomain(SubscriptionEntity e) {
        return new Subscription(e.id, e.clientId, e.fundId, e.amount, e.subscribedAt, e.active);
    }

    private SubscriptionEntity toEntity(Subscription s) {
        SubscriptionEntity e = new SubscriptionEntity();
        e.id = s.id();
        e.clientId = s.clientId();
        e.fundId = s.fundId();
        e.amount = s.amount();
        e.subscribedAt = s.subscribedAt();
        e.active = s.active();
        return e;
    }
}
