package com.btg.funds.infrastructure.adapter.in.rest;

import com.btg.funds.domain.exception.NotFoundException;
import com.btg.funds.domain.port.out.ClientRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class ClientIdentityResolver {

    private final ClientRepositoryPort clientRepositoryPort;

    public ClientIdentityResolver(ClientRepositoryPort clientRepositoryPort) {
        this.clientRepositoryPort = clientRepositoryPort;
    }

    public String resolveClientId(String documentNumber) {
        return clientRepositoryPort.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"))
                .id();
    }
}
