package com.btg.funds.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.btg.funds.domain.exception.InsufficientBalanceException;
import com.btg.funds.domain.model.Client;
import com.btg.funds.domain.model.Fund;
import com.btg.funds.domain.model.NotificationChannel;
import com.btg.funds.domain.model.Role;
import com.btg.funds.domain.model.Subscription;
import com.btg.funds.domain.port.out.ClientRepositoryPort;
import com.btg.funds.domain.port.out.FundRepositoryPort;
import com.btg.funds.domain.port.out.NotificationPort;
import com.btg.funds.domain.port.out.SubscriptionRepositoryPort;
import com.btg.funds.domain.port.out.TransactionRepositoryPort;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private ClientRepositoryPort clientRepositoryPort;
    @Mock
    private FundRepositoryPort fundRepositoryPort;
    @Mock
    private SubscriptionRepositoryPort subscriptionRepositoryPort;
    @Mock
    private TransactionRepositoryPort transactionRepositoryPort;
    @Mock
    private NotificationPort notificationPort;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private Client client;
    private Fund fund;

    @BeforeEach
    void setup() {
        client = new Client("c1", "1", "User", "a@a.com", "+57", "x", Set.of(Role.USER), new BigDecimal("500000"));
        fund = new Fund("1", "FPV_BTG_PACTUAL_RECAUDADORA", new BigDecimal("75000"), "FPV");
    }

    @Test
    void shouldSubscribeAndDiscountBalance() {
        when(clientRepositoryPort.findById("c1")).thenReturn(java.util.Optional.of(client));
        when(fundRepositoryPort.findById("1")).thenReturn(java.util.Optional.of(fund));
        when(subscriptionRepositoryPort.save(any())).thenReturn(
                new Subscription("s1", "c1", "1", fund.minimumAmount(), Instant.now(), true)
        );

        Subscription result = subscriptionService.subscribe("c1", "1", NotificationChannel.EMAIL);
        assertEquals("s1", result.id());
        verify(clientRepositoryPort).save(any());
        verify(transactionRepositoryPort).save(any());
        verify(notificationPort).send(any(), any(), any());
    }

    @Test
    void shouldFailWhenInsufficientBalance() {
        Client lowBalance = new Client("c1", "1", "User", "a@a.com", "+57", "x", Set.of(Role.USER), new BigDecimal("1000"));
        when(clientRepositoryPort.findById("c1")).thenReturn(java.util.Optional.of(lowBalance));
        when(fundRepositoryPort.findById("1")).thenReturn(java.util.Optional.of(fund));

        assertThrows(InsufficientBalanceException.class,
                () -> subscriptionService.subscribe("c1", "1", NotificationChannel.SMS));
    }
}
