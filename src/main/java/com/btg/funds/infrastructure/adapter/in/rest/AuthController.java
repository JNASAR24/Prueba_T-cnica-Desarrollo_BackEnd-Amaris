package com.btg.funds.infrastructure.adapter.in.rest;

import com.btg.funds.domain.port.in.AuthUseCase;
import com.btg.funds.infrastructure.adapter.in.rest.dto.AuthRequest;
import com.btg.funds.infrastructure.adapter.in.rest.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth")
public class AuthController {

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario y obtener JWT")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return new AuthResponse(authUseCase.login(request.documentNumber(), request.password()));
    }
}
