package com.btg.funds.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.btg.funds.domain.model.Client;
import com.btg.funds.domain.model.Role;
import com.btg.funds.domain.port.out.ClientRepositoryPort;
import com.btg.funds.domain.port.out.TokenProviderPort;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private ClientRepositoryPort clientRepositoryPort;
    @Mock
    private TokenProviderPort tokenProviderPort;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthService authService;

    @Test
    void shouldReturnTokenOnValidCredentials() {
        Client c = new Client("c1", "123", "User", "a@a.com", "1", "hash", Set.of(Role.USER), new BigDecimal("500000"));
        when(clientRepositoryPort.findByDocumentNumber("123")).thenReturn(Optional.of(c));
        when(passwordEncoder.matches("pwd", "hash")).thenReturn(true);
        when(tokenProviderPort.generateToken(c)).thenReturn("jwt");

        assertEquals("jwt", authService.login("123", "pwd"));
    }
}
