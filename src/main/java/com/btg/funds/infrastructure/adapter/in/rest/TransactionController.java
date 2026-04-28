package com.btg.funds.infrastructure.adapter.in.rest;

import com.btg.funds.domain.model.Transaction;
import com.btg.funds.domain.port.in.TransactionQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transactions")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionQueryUseCase transactionQueryUseCase;
    private final ClientIdentityResolver clientIdentityResolver;

    public TransactionController(
            TransactionQueryUseCase transactionQueryUseCase,
            ClientIdentityResolver clientIdentityResolver
    ) {
        this.transactionQueryUseCase = transactionQueryUseCase;
        this.clientIdentityResolver = clientIdentityResolver;
    }

    @GetMapping
    @Operation(summary = "Consultar historial de transacciones del cliente autenticado")
    public List<Transaction> history(@AuthenticationPrincipal User user) {
        return transactionQueryUseCase.findByClientId(clientIdentityResolver.resolveClientId(user.getUsername()));
    }
}
