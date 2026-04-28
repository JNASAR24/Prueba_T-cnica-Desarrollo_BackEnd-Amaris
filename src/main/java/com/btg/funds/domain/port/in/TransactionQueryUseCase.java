package com.btg.funds.domain.port.in;

import com.btg.funds.domain.model.Transaction;
import java.util.List;

public interface TransactionQueryUseCase {
    List<Transaction> findByClientId(String clientId);
}
