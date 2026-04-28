package com.btg.funds.infrastructure.security;

import com.btg.funds.domain.port.out.ClientRepositoryPort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientUserDetailsService implements UserDetailsService {

    private final ClientRepositoryPort clientRepositoryPort;

    public ClientUserDetailsService(ClientRepositoryPort clientRepositoryPort) {
        this.clientRepositoryPort = clientRepositoryPort;
    }

    @Override
    public UserDetails loadUserByUsername(String documentNumber) throws UsernameNotFoundException {
        return clientRepositoryPort.findByDocumentNumber(documentNumber)
                .map(client -> new User(
                        client.documentNumber(),
                        client.passwordHash(),
                        client.roles().stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.name())).toList()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
