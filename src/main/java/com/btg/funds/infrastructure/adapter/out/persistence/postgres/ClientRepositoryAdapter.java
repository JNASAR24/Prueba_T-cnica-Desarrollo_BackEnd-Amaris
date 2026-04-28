package com.btg.funds.infrastructure.adapter.out.persistence.postgres;

import com.btg.funds.domain.model.Client;
import com.btg.funds.domain.model.Role;
import com.btg.funds.domain.port.out.ClientRepositoryPort;
import com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity.ClientEntity;
import com.btg.funds.infrastructure.adapter.out.persistence.postgres.repository.SpringClientRepository;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ClientRepositoryAdapter implements ClientRepositoryPort {

    private final SpringClientRepository repository;

    public ClientRepositoryAdapter(SpringClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Client> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Client> findByDocumentNumber(String documentNumber) {
        return repository.findByDocumentNumber(documentNumber).map(this::toDomain);
    }

    @Override
    public Client save(Client client) {
        return toDomain(repository.save(toEntity(client)));
    }

    private Client toDomain(ClientEntity e) {
        return new Client(e.id, e.documentNumber, e.fullName, e.email, e.phone, e.passwordHash,
                e.roles.stream().map(Role::valueOf).collect(Collectors.toSet()), e.availableBalance);
    }

    private ClientEntity toEntity(Client c) {
        ClientEntity e = new ClientEntity();
        e.id = c.id();
        e.documentNumber = c.documentNumber();
        e.fullName = c.fullName();
        e.email = c.email();
        e.phone = c.phone();
        e.passwordHash = c.passwordHash();
        Set<String> roles = c.roles().stream().map(Enum::name).collect(Collectors.toSet());
        e.roles = roles;
        e.availableBalance = c.availableBalance();
        return e;
    }
}
