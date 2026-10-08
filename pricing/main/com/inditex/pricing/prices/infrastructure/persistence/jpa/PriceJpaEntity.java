package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.inditex.pricing.brands.infrastructure.persistence.jpa.BrandJpaEntity;
import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.products.infrastructure.persistence.jpa.ProductJpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "prices")
public class PriceJpaEntity implements Persistable<UUID> {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "brand_id", nullable = false)
  private BrandJpaEntity brand;

  @Column(name = "start_date", nullable = false)
  private LocalDateTime startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDateTime endDate;

  @Column(name = "price_list", nullable = false)
  private Integer priceList;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_id", nullable = false)
  private ProductJpaEntity product;

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
      BrandJpaEntity brand,
      LocalDateTime startDate,
      LocalDateTime endDate,
      Integer priceList,
      ProductJpaEntity product,
      Integer priority,
      BigDecimal price,
      String currency,
      boolean newEntity) {
    this.id = id;
    this.brand = brand;
    this.startDate = startDate;
    this.endDate = endDate;
    this.priceList = priceList;
    this.product = product;
    this.priority = priority;
    this.price = price;
    this.currency = currency;
    this.newEntity = newEntity;
  }

  public static PriceJpaEntity forCreate(
      Price price, BrandJpaEntity brand, ProductJpaEntity product) {
    return fromDomain(price, brand, product, true);
  }

  public static PriceJpaEntity forUpdate(
      Price price, BrandJpaEntity brand, ProductJpaEntity product) {
    return fromDomain(price, brand, product, false);
  }

  private static PriceJpaEntity fromDomain(
      Price price, BrandJpaEntity brand, ProductJpaEntity product, boolean newEntity) {
    return new PriceJpaEntity(
        price.id().toUuid(),
        brand,
        price.startDate().value(),
        price.endDate().value(),
        price.priceList().value(),
        product,
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
    return brand.getId();
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
    return product.getId();
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
