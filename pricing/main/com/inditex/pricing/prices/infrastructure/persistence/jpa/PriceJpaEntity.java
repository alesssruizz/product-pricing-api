package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.inditex.pricing.prices.domain.Price;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "prices")
public class PriceJpaEntity implements Persistable<UUID> {

  @Id private UUID id;

  @Column(name = "brand_id", nullable = false)
  private Long brandId;

  @Column(name = "start_date", nullable = false)
  private LocalDateTime startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDateTime endDate;

  @Column(name = "price_list", nullable = false)
  private Integer priceList;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(nullable = false)
  private Integer priority;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(name = "curr", columnDefinition = "CHAR(3)", nullable = false)
  private String currency;

  @Transient private boolean newEntity;

  protected PriceJpaEntity() {
    // Required by JPA
  }

  private PriceJpaEntity(
      UUID id,
      Long brandId,
      LocalDateTime startDate,
      LocalDateTime endDate,
      Integer priceList,
      Long productId,
      Integer priority,
      BigDecimal price,
      String currency,
      boolean newEntity) {
    this.id = id;
    this.brandId = brandId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.priceList = priceList;
    this.productId = productId;
    this.priority = priority;
    this.price = price;
    this.currency = currency;
    this.newEntity = newEntity;
  }

  public static PriceJpaEntity forCreate(Price price) {
    return fromDomain(price, true);
  }

  public static PriceJpaEntity forUpdate(Price price) {
    return fromDomain(price, false);
  }

  private static PriceJpaEntity fromDomain(Price price, boolean newEntity) {
    return new PriceJpaEntity(
        price.id().toUuid(),
        price.brandId().value(),
        price.startDate().value(),
        price.endDate().value(),
        price.priceList().value(),
        price.productId().value(),
        price.priority().value(),
        price.priceAmount().value(),
        price.currency().value(),
        newEntity);
  }

  @Override
  public UUID getId() {
    return id;
  }

  public Long getBrandId() {
    return brandId;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public Integer getPriceList() {
    return priceList;
  }

  public Long getProductId() {
    return productId;
  }

  public Integer getPriority() {
    return priority;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public String getCurrency() {
    return currency;
  }

  @Override
  public boolean isNew() {
    return newEntity;
  }
}
