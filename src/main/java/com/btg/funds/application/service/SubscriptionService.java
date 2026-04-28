package com.btg.funds.application.service;

import com.btg.funds.domain.exception.InsufficientBalanceException;
import com.btg.funds.domain.exception.NotFoundException;
import com.btg.funds.domain.model.Client;
import com.btg.funds.domain.model.Fund;
import com.btg.funds.domain.model.NotificationChannel;
import com.btg.funds.domain.model.Subscription;
import com.btg.funds.domain.model.Transaction;
import com.btg.funds.domain.model.TransactionType;
import com.btg.funds.domain.port.in.SubscriptionUseCase;
import com.btg.funds.domain.port.out.ClientRepositoryPort;
import com.btg.funds.domain.port.out.FundRepositoryPort;
import com.btg.funds.domain.port.out.NotificationPort;
import com.btg.funds.domain.port.out.SubscriptionRepositoryPort;
import com.btg.funds.domain.port.out.TransactionRepositoryPort;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService implements SubscriptionUseCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionService.class);

    private final ClientRepositoryPort clientRepositoryPort;
    private final FundRepositoryPort fundRepositoryPort;
    private final SubscriptionRepositoryPort subscriptionRepositoryPort;
    private final TransactionRepositoryPort transactionRepositoryPort;
    private final NotificationPort notificationPort;

    public SubscriptionService(
            ClientRepositoryPort clientRepositoryPort,
            FundRepositoryPort fundRepositoryPort,
            SubscriptionRepositoryPort subscriptionRepositoryPort,
            TransactionRepositoryPort transactionRepositoryPort,
            NotificationPort notificationPort
    ) {
        this.clientRepositoryPort = clientRepositoryPort;
        this.fundRepositoryPort = fundRepositoryPort;
        this.subscriptionRepositoryPort = subscriptionRepositoryPort;
        this.transactionRepositoryPort = transactionRepositoryPort;
        this.notificationPort = notificationPort;
    }

    @Override
    public Subscription subscribe(String clientId, String fundId, NotificationChannel channel) {
        LOGGER.info("Subscription requested clientId={} fundId={} channel={}", clientId, fundId, channel);
        Client client = clientRepositoryPort.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        Fund fund = fundRepositoryPort.findById(fundId)
                .orElseThrow(() -> new NotFoundException("Fondo no encontrado"));

        if (client.availableBalance().compareTo(fund.minimumAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "No tiene saldo disponible para vincularse al fondo " + fund.name()
            );
        }

        Client updatedClient = new Client(
                client.id(),
                client.documentNumber(),
                client.fullName(),
                client.email(),
                client.phone(),
                client.passwordHash(),
                client.roles(),
                client.availableBalance().subtract(fund.minimumAmount())
        );
        clientRepositoryPort.save(updatedClient);

        Subscription subscription = subscriptionRepositoryPort.save(new Subscription(
                null,
                client.id(),
                fund.id(),
                fund.minimumAmount(),
                Instant.now(),
                true
        ));

        transactionRepositoryPort.save(new Transaction(
                UUID.randomUUID().toString(),
                client.id(),
                subscription.id(),
                fund.id(),
                TransactionType.SUBSCRIPTION,
                fund.minimumAmount(),
                Instant.now()
        ));

        notificationPort.send(resolveDestination(updatedClient, channel), channel,
                "Suscripcion creada para fondo " + fund.name() + " por " + fund.minimumAmount() + " COP");
        LOGGER.info("Subscription created subscriptionId={} clientId={} fundId={}", subscription.id(), client.id(), fund.id());
        return subscription;
    }

    @Override
    public void cancel(String clientId, String subscriptionId, NotificationChannel channel) {
        LOGGER.info("Cancellation requested clientId={} subscriptionId={} channel={}", clientId, subscriptionId, channel);
        Client client = clientRepositoryPort.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        Subscription subscription = subscriptionRepositoryPort.findById(subscriptionId)
                .orElseThrow(() -> new NotFoundException("Suscripcion no encontrada"));
        Fund fund = fundRepositoryPort.findById(subscription.fundId())
                .orElseThrow(() -> new NotFoundException("Fondo no encontrado"));
        if (!subscription.clientId().equals(clientId) || !subscription.active()) {
            throw new NotFoundException("Suscripcion no encontrada para el cliente");
        }

        Client updatedClient = new Client(
                client.id(),
                client.documentNumber(),
                client.fullName(),
                client.email(),
                client.phone(),
                client.passwordHash(),
                client.roles(),
                client.availableBalance().add(subscription.amount())
        );
        clientRepositoryPort.save(updatedClient);

        subscriptionRepositoryPort.save(new Subscription(
                subscription.id(),
                subscription.clientId(),
                subscription.fundId(),
                subscription.amount(),
                subscription.subscribedAt(),
                false
        ));

        transactionRepositoryPort.save(new Transaction(
                UUID.randomUUID().toString(),
                client.id(),
                subscription.id(),
                subscription.fundId(),
                TransactionType.CANCELLATION,
                subscription.amount(),
                Instant.now()
        ));

        notificationPort.send(resolveDestination(updatedClient, channel), channel,
                "Suscripcion cancelada para fondo " + fund.name() + ". Monto devuelto: " + subscription.amount());
        LOGGER.info("Subscription cancelled subscriptionId={} clientId={} amount={}", subscription.id(), client.id(), subscription.amount());
    }

    private String resolveDestination(Client client, NotificationChannel channel) {
        return channel == NotificationChannel.EMAIL ? client.email() : client.phone();
    }
}
