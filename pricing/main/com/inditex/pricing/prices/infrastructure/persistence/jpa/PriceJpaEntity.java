package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "prices")
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class PriceJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "brand_id")
  private final Long brandId;

  @Column(name = "start_date")
  private final LocalDateTime startDate;

  @Column(name = "end_date")
  private final LocalDateTime endDate;

  @Column(name = "price_list")
  private final Integer priceList;

  @Column(name = "product_id")
  private final Long productId;

  private final Integer priority;

  private final BigDecimal price;

  @Column(name = "curr", columnDefinition = "CHAR(3)")
  private final String currency;
}
