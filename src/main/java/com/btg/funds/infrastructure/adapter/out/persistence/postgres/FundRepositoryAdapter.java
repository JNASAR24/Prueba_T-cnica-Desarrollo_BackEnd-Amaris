package com.btg.funds.infrastructure.adapter.out.persistence.postgres;

import com.btg.funds.domain.model.Fund;
import com.btg.funds.domain.port.out.FundRepositoryPort;
import com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity.FundEntity;
import com.btg.funds.infrastructure.adapter.out.persistence.postgres.repository.SpringFundRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class FundRepositoryAdapter implements FundRepositoryPort {

    private final SpringFundRepository repository;

    public FundRepositoryAdapter(SpringFundRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Fund> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Fund> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    private Fund toDomain(FundEntity e) {
        return new Fund(e.id, e.name, e.minimumAmount, e.category);
    }
}
