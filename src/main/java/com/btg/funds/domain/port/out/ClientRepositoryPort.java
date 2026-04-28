package com.btg.funds.domain.port.out;

import com.btg.funds.domain.model.Client;
import java.util.Optional;

public interface ClientRepositoryPort {
    Optional<Client> findById(String id);
    Optional<Client> findByDocumentNumber(String documentNumber);
    Client save(Client client);
}
