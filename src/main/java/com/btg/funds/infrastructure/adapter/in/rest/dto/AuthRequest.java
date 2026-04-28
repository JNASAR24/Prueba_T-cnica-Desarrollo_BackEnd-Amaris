package com.btg.funds.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank String documentNumber,
        @NotBlank String password
) {
}
