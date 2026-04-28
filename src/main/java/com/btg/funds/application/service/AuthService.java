package com.btg.funds.application.service;

import com.btg.funds.domain.exception.DomainException;
import com.btg.funds.domain.model.Client;
import com.btg.funds.domain.port.in.AuthUseCase;
import com.btg.funds.domain.port.out.ClientRepositoryPort;
import com.btg.funds.domain.port.out.TokenProviderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final ClientRepositoryPort clientRepositoryPort;
    private final TokenProviderPort tokenProviderPort;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            ClientRepositoryPort clientRepositoryPort,
            TokenProviderPort tokenProviderPort,
            PasswordEncoder passwordEncoder
    ) {
        this.clientRepositoryPort = clientRepositoryPort;
        this.tokenProviderPort = tokenProviderPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(String documentNumber, String rawPassword) {
        LOGGER.info("Login attempt for documentNumber={}", documentNumber);
        Client client = clientRepositoryPort.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new DomainException("Credenciales invalidas"));
        if (!passwordEncoder.matches(rawPassword, client.passwordHash())) {
            LOGGER.warn("Login failed for documentNumber={}", documentNumber);
            throw new DomainException("Credenciales invalidas");
        }
        LOGGER.info("Login success for clientId={}", client.id());
        return tokenProviderPort.generateToken(client);
    }
}
