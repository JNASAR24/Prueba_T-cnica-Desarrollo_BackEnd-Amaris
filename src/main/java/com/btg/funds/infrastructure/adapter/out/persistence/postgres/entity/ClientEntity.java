package com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "clients")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;
    @Column(nullable = false, unique = true)
    public String documentNumber;
    @Column(nullable = false)
    public String fullName;
    @Column(nullable = false)
    public String email;
    @Column(nullable = false)
    public String phone;
    @Column(nullable = false)
    public String passwordHash;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_roles", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "role")
    public Set<String> roles;
    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal availableBalance;
}
