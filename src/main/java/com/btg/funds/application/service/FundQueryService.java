package com.btg.funds.application.service;

import com.btg.funds.domain.model.Fund;
import com.btg.funds.domain.port.in.FundQueryUseCase;
import com.btg.funds.domain.port.out.FundRepositoryPort;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FundQueryService implements FundQueryUseCase {

    private final FundRepositoryPort fundRepositoryPort;

    public FundQueryService(FundRepositoryPort fundRepositoryPort) {
        this.fundRepositoryPort = fundRepositoryPort;
    }

    @Override
    public List<Fund> getFunds() {
        return fundRepositoryPort.findAll();
    }
}
