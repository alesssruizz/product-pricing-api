package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "prices")
public class PriceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "price_list")
    private Integer priceList;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "curr", columnDefinition = "CHAR(3)")
    private String currency;

    protected PriceJpaEntity() {}

    public Long id() {
        return id;
    }

    public Long brandId() {
        return brandId;
    }

    public LocalDateTime startDate() {
        return startDate;
    }

    public LocalDateTime endDate() {
        return endDate;
    }

    public Integer priceList() {
        return priceList;
    }

    public Long productId() {
        return productId;
    }

    public Integer priority() {
        return priority;
    }

    public BigDecimal price() {
        return price;
    }

    public String currency() {
        return currency;
    }
}
