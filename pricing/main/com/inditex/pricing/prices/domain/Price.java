package com.inditex.pricing.prices.domain;

import java.math.BigDecimal;
import java.util.Objects;

import com.inditex.pricing.prices.domain.event.PriceCreatedDomainEvent;
import com.inditex.pricing.prices.domain.event.PriceDeletedDomainEvent;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceDateRange;
import com.inditex.pricing.prices.domain.exceptions.PriceFieldRequired;
import com.inditex.pricing.shared.domain.AggregateRoot;

import lombok.Getter;

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

  public static Price create(
      String id,
      Long brandId,
      Long productId,
      Integer priceList,
      Integer priority,
      String startDate,
      String endDate,
      BigDecimal price,
      String currency) {

    var newPrice =
        new Price(
            required(id, "id"),
            required(brandId, "brandId"),
            required(productId, "productId"),
            required(priceList, "priceList"),
            required(priority, "priority"),
            required(startDate, "startDate"),
            required(endDate, "endDate"),
            required(price, "price"),
            required(currency, "currency"));

    newPrice.register(
        new PriceCreatedDomainEvent(
            id, brandId, productId, priceList, priority, startDate, endDate, price, currency));

    return newPrice;
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

  private static <T> T required(T value, String field) {
    if (Objects.isNull(value) || (value instanceof String str && str.isBlank())) {
      throw new PriceFieldRequired(field);
    }
    return value;
  }
}
