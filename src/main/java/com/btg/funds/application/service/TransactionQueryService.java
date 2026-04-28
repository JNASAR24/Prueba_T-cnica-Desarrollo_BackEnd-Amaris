package com.btg.funds.application.service;

import com.btg.funds.domain.model.Transaction;
import com.btg.funds.domain.port.in.TransactionQueryUseCase;
import com.btg.funds.domain.port.out.TransactionRepositoryPort;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionQueryService implements TransactionQueryUseCase {

    private final TransactionRepositoryPort transactionRepositoryPort;

    public TransactionQueryService(TransactionRepositoryPort transactionRepositoryPort) {
        this.transactionRepositoryPort = transactionRepositoryPort;
    }

    @Override
    public List<Transaction> findByClientId(String clientId) {
        return transactionRepositoryPort.findByClientId(clientId);
    }
}
