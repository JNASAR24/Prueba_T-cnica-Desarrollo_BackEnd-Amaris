package com.btg.funds.infrastructure.adapter.in.rest;

import com.btg.funds.domain.model.Fund;
import com.btg.funds.domain.port.in.FundQueryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/funds")
@Tag(name = "Funds")
@SecurityRequirement(name = "bearerAuth")
public class FundController {

    private final FundQueryUseCase fundQueryUseCase;

    public FundController(FundQueryUseCase fundQueryUseCase) {
        this.fundQueryUseCase = fundQueryUseCase;
    }

    @GetMapping
    @Operation(summary = "Listar fondos disponibles")
    public List<Fund> getFunds() {
        return fundQueryUseCase.getFunds();
    }
}
