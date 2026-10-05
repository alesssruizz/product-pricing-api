package com.inditex.pricing.prices.domain;

import java.math.BigDecimal;
import java.util.Objects;

import com.inditex.pricing.prices.domain.exceptions.InvalidPriceDateRange;
import com.inditex.pricing.prices.domain.exceptions.PriceFieldRequired;
import com.inditex.pricing.shared.domain.StringValueObject;

import lombok.Builder;

@Builder
public record Price(
    PriceId id,
    PriceBrandId brandId,
    PriceDate startDate,
    PriceDate endDate,
    PriceList priceList,
    PriceProductId productId,
    PricePriority priority,
    PriceQuantity priceQuantity,
    PriceCurrency currency) {

  public Price {
    if (!endDate.value().isAfter(startDate.value())) {
      throw new InvalidPriceDateRange();
    }
  }

  public static Price create(
      Long brandId,
      Long productId,
      Integer priceList,
      Integer priority,
      String startDate,
      String endDate,
      BigDecimal price,
      String currency) {
    final PriceBrandId brand = new PriceBrandId(required(brandId, "brandId"));
    final PriceProductId product = new PriceProductId(required(productId, "productId"));
    final PriceDate start = new PriceDate(required(startDate, "startDate"));
    final PriceDate end = new PriceDate(required(endDate, "endDate"));
    final PriceList list = new PriceList(required(priceList, "priceList"));
    final PricePriority rank = new PricePriority(required(priority, "priority"));
    final PriceQuantity quantity = new PriceQuantity(required(price, "price"));
    final PriceCurrency currencyCode = new PriceCurrency(required(currency, "currency"));

    return Price.builder()
        .brandId(brand)
        .startDate(start)
        .endDate(end)
        .priceList(list)
        .productId(product)
        .priority(rank)
        .priceQuantity(quantity)
        .currency(currencyCode)
        .build();
  }

  public Price withId(PriceId id) {
    return new Price(
        id, brandId, startDate, endDate, priceList, productId, priority, priceQuantity, currency);
  }

  private static <T> T required(T value, String field) {
    if (Objects.isNull(value)
        || (value instanceof StringValueObject str && str.value().isBlank())) {
      throw new PriceFieldRequired(field);
    }
    return value;
  }
}
