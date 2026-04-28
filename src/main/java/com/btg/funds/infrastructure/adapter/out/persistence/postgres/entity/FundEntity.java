package com.btg.funds.infrastructure.adapter.out.persistence.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "funds")
public class FundEntity {
    @Id
    public String id;
    @Column(nullable = false, unique = true)
    public String name;
    @Column(nullable = false, precision = 19, scale = 2)
    public BigDecimal minimumAmount;
    @Column(nullable = false)
    public String category;
}
