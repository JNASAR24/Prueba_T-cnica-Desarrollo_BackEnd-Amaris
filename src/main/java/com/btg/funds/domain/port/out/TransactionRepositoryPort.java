package com.btg.funds.domain.port.out;

import com.btg.funds.domain.model.Transaction;
import java.util.List;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
    List<Transaction> findByClientId(String clientId);
}
