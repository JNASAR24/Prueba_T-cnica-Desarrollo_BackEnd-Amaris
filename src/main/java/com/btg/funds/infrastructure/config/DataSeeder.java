package com.btg.funds.infrastructure.config;

import com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity.ClientEntity;
import com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity.FundEntity;
import com.btg.funds.infrastructure.adapter.out.persistence.postgres.repository.SpringClientRepository;
import com.btg.funds.infrastructure.adapter.out.persistence.postgres.repository.SpringFundRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SpringFundRepository fundRepository;
    private final SpringClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            SpringFundRepository fundRepository,
            SpringClientRepository clientRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.fundRepository = fundRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (fundRepository.count() == 0) {
            fundRepository.saveAll(List.of(
                    fund("1", "FPV_BTG_PACTUAL_RECAUDADORA", 75000, "FPV"),
                    fund("2", "FPV_BTG_PACTUAL_ECOPETROL", 125000, "FPV"),
                    fund("3", "DEUDAPRIVADA", 50000, "FIC"),
                    fund("4", "FDO-ACCIONES", 250000, "FIC"),
                    fund("5", "FPV_BTG_PACTUAL_DINAMICA", 100000, "FPV")
            ));
        }

        if (clientRepository.findByDocumentNumber("123456789").isEmpty()) {
            ClientEntity user = new ClientEntity();
            user.documentNumber = "123456789";
            user.fullName = "Usuario Demo";
            user.email = "demo@btg.com";
            user.phone = "+573001112233";
            user.passwordHash = passwordEncoder.encode("ChangeMe123!");
            user.roles = Set.of("USER");
            user.availableBalance = new BigDecimal("500000");
            clientRepository.save(user);
        }
    }

    private FundEntity fund(String id, String name, int min, String category) {
        FundEntity f = new FundEntity();
        f.id = id;
        f.name = name;
        f.minimumAmount = new BigDecimal(min);
        f.category = category;
        return f;
    }
}
