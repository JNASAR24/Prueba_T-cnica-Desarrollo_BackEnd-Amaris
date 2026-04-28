package com.btg.funds.domain.port.out;

import com.btg.funds.domain.model.Client;

public interface TokenProviderPort {
    String generateToken(Client client);
}
