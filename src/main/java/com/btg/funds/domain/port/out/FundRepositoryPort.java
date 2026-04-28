package com.btg.funds.domain.port.out;

import com.btg.funds.domain.model.Fund;
import java.util.List;
import java.util.Optional;

public interface FundRepositoryPort {
    List<Fund> findAll();
    Optional<Fund> findById(String id);
}
