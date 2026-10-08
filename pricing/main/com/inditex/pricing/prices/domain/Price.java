package com.inditex.pricing.prices.domain;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.event.PriceCreatedDomainEvent;
import com.inditex.pricing.prices.domain.event.PriceDeletedDomainEvent;
import com.inditex.pricing.prices.domain.event.PriceUpdatedDomainEvent;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceDateRange;
import com.inditex.pricing.shared.domain.AggregateRoot;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false)
@Getter
public final class Price extends AggregateRoot {

  private final PriceId id;

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

  private Price(String id) {
    this.id = new PriceId(id);
    this.brandId = null;
    this.startDate = null;
    this.endDate = null;
    this.priceList = null;
    this.productId = null;
    this.priority = null;
    this.priceAmount = null;
    this.currency = null;
  }

  public static Price create(
      String id,
      Long brandId,
      String startDate,
      String endDate,
      Integer priceList,
      Long productId,
      Integer priority,
      BigDecimal price,
      String currency) {

    var newPrice =
        new Price(id, brandId, productId, priceList, priority, startDate, endDate, price, currency);

    newPrice.register(
        new PriceCreatedDomainEvent(
            id, brandId, productId, priceList, priority, startDate, endDate, price, currency));

    return newPrice;
  }

  public static Price update(
      String id,
      Long brandId,
      String startDate,
      String endDate,
      Integer priceList,
      Long productId,
      Integer priority,
      BigDecimal price,
      String currency) {

    var updatedPrice =
        new Price(id, brandId, productId, priceList, priority, startDate, endDate, price, currency);

    updatedPrice.register(
        new PriceUpdatedDomainEvent(
            id, brandId, productId, priceList, priority, startDate, endDate, price, currency));

    return updatedPrice;
  }

  public static Price delete(String id) {
    var deletedPrice = new Price(id);
    deletedPrice.register(new PriceDeletedDomainEvent(id));

    return deletedPrice;
  }

  private void ensureValidDateRange() {
    if (!endDate.value().isAfter(startDate.value())) {
      throw new InvalidPriceDateRange();
    }
  }
}
