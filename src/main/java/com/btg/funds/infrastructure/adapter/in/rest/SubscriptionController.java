package com.btg.funds.infrastructure.adapter.in.rest;

import com.btg.funds.domain.model.Subscription;
import com.btg.funds.domain.port.in.SubscriptionUseCase;
import com.btg.funds.infrastructure.adapter.in.rest.dto.CancelSubscriptionRequest;
import com.btg.funds.infrastructure.adapter.in.rest.dto.CancelSubscriptionResponse;
import com.btg.funds.infrastructure.adapter.in.rest.dto.SubscriptionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscriptions")
@Tag(name = "Subscriptions")
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {

    private final SubscriptionUseCase subscriptionUseCase;
    private final ClientIdentityResolver clientIdentityResolver;

    public SubscriptionController(SubscriptionUseCase subscriptionUseCase, ClientIdentityResolver clientIdentityResolver) {
        this.subscriptionUseCase = subscriptionUseCase;
        this.clientIdentityResolver = clientIdentityResolver;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Suscribirse a un fondo")
    public Subscription subscribe(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SubscriptionRequest request
    ) {
        return subscriptionUseCase.subscribe(
                clientIdentityResolver.resolveClientId(user.getUsername()),
                request.fundId(),
                request.notificationChannel()
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar suscripcion y devolver saldo")
    public ResponseEntity<CancelSubscriptionResponse> cancel(
            @AuthenticationPrincipal User user,
            @PathVariable String id,
            @Valid @RequestBody CancelSubscriptionRequest request
    ) {
        subscriptionUseCase.cancel(
                clientIdentityResolver.resolveClientId(user.getUsername()),
                id,
                request.notificationChannel()
        );
        return ResponseEntity.ok(new CancelSubscriptionResponse(
                id,
                "Suscripcion cancelada exitosamente y saldo devuelto al cliente"
        ));
    }
}
