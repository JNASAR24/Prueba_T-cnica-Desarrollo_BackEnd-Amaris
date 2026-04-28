package com.btg.funds.domain.port.in;

public interface AuthUseCase {
    String login(String documentNumber, String rawPassword);
}
