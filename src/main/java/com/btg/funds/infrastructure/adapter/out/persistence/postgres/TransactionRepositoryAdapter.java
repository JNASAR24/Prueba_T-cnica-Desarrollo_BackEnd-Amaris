package com.btg.funds.infrastructure.adapter.out.persistence.postgres;

import com.btg.funds.domain.model.Transaction;
import com.btg.funds.domain.model.TransactionType;
import com.btg.funds.domain.port.out.TransactionRepositoryPort;
import com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity.TransactionEntity;
import com.btg.funds.infrastructure.adapter.out.persistence.postgres.repository.SpringTransactionRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final SpringTransactionRepository repository;

    public TransactionRepositoryAdapter(SpringTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return toDomain(repository.save(toEntity(transaction)));
    }

    @Override
    public List<Transaction> findByClientId(String clientId) {
        return repository.findByClientIdOrderByCreatedAtDesc(clientId).stream().map(this::toDomain).toList();
    }

    private Transaction toDomain(TransactionEntity e) {
        return new Transaction(e.id, e.clientId, e.subscriptionId, e.fundId,
                TransactionType.valueOf(e.type), e.amount, e.createdAt);
    }

    private TransactionEntity toEntity(Transaction t) {
        TransactionEntity e = new TransactionEntity();
        e.id = t.id();
        e.clientId = t.clientId();
        e.subscriptionId = t.subscriptionId();
        e.fundId = t.fundId();
        e.type = t.type().name();
        e.amount = t.amount();
        e.createdAt = t.createdAt();
        return e;
    }
}
