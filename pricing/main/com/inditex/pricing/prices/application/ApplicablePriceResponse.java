package com.inditex.pricing.prices.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.shared.domain.bus.query.Response;

public record ApplicablePriceResponse(
    Long productId,
    Long brandId,
    Integer priceList,
    LocalDateTime startDate,
    LocalDateTime endDate,
    BigDecimal price,
    String currency)
    implements Response {
  public static ApplicablePriceResponse fromAggregate(Price price) {
    return new ApplicablePriceResponse(
        price.productId().value(),
        price.brandId().value(),
        price.priceList().value(),
        price.startDate().value(),
        price.endDate().value(),
        price.priceQuantity().value(),
        price.currency().value());
  }
}
