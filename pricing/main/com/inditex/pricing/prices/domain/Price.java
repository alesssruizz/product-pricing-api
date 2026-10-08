package com.inditex.pricing.prices.domain;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.event.PriceCreatedDomainEvent;
import com.inditex.pricing.prices.domain.event.PriceDeletedDomainEvent;
import com.inditex.pricing.prices.domain.event.PriceUpdatedDomainEvent;
import com.inditex.pricing.prices.domain.exception.InvalidPriceDateRange;
import com.inditex.pricing.shared.domain.AggregateRoot;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Price extends AggregateRoot {

  @EqualsAndHashCode.Include private final PriceId id;

  private final BrandId brandId;

  private final PriceDate startDate;

  private final PriceDate endDate;

  private final PriceList priceList;

  private final ProductId productId;

  private final PricePriority priority;

  private final PriceAmount priceAmount;

  private final PriceCurrency currency;

  public Price(
      String id,
      Long brandId,
      Long productId,
      Integer priceList,
      Integer priority,
      String startDate,
      String endDate,
      BigDecimal priceAmount,
      String currency) {
    this.id = new PriceId(id);
    this.brandId = new BrandId(brandId);
    this.startDate = new PriceDate(startDate);
    this.endDate = new PriceDate(endDate);
    this.priceList = new PriceList(priceList);
    this.productId = new ProductId(productId);
    this.priority = new PricePriority(priority);
    this.priceAmount = new PriceAmount(priceAmount);
    this.currency = new PriceCurrency(currency);
    ensureValidDateRange();
  }

  public static Price create(
      PriceId id,
      BrandId brandId,
      PriceDate startDate,
      PriceDate endDate,
      PriceList priceList,
      ProductId productId,
      PricePriority priority,
      PriceAmount price,
      PriceCurrency currency) {

    var newPrice =
        new Price(id, brandId, startDate, endDate, priceList, productId, priority, price, currency);
    newPrice.ensureValidDateRange();

    newPrice.register(
        new PriceCreatedDomainEvent(
            id.value(),
            brandId.value(),
            productId.value(),
            priceList.value(),
            priority.value(),
            startDate.value().toString(),
            endDate.value().toString(),
            price.value(),
            currency.value()));

    return newPrice;
  }

  public static Price update(
      PriceId id,
      BrandId brandId,
      PriceDate startDate,
      PriceDate endDate,
      PriceList priceList,
      ProductId productId,
      PricePriority priority,
      PriceAmount price,
      PriceCurrency currency) {

    var updatedPrice =
        new Price(id, brandId, startDate, endDate, priceList, productId, priority, price, currency);
    updatedPrice.ensureValidDateRange();

    updatedPrice.register(
        new PriceUpdatedDomainEvent(
            id.value(),
            brandId.value(),
            productId.value(),
            priceList.value(),
            priority.value(),
            startDate.value().toString(),
            endDate.value().toString(),
            price.value(),
            currency.value()));

    return updatedPrice;
  }

  public static Price delete(PriceId id) {
    var deletedPrice = new Price(id, null, null, null, null, null, null, null, null);
    deletedPrice.register(new PriceDeletedDomainEvent(id.value()));

    return deletedPrice;
  }

  private void ensureValidDateRange() {
    if (!endDate.value().isAfter(startDate.value())) {
      throw new InvalidPriceDateRange();
    }
  }
}
