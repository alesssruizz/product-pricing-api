package com.inditex.pricing.prices.domain.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import com.inditex.pricing.shared.domain.bus.event.DomainEvent;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false)
@Getter
public class PriceUpdatedDomainEvent extends DomainEvent {
  private final Long productId;

  private final Long brandId;

  private final Integer priceList;

  private final Integer priority;

  private final String startDate;

  private final String endDate;

  private final BigDecimal price;

  private final String currency;

  public PriceUpdatedDomainEvent(
      String aggregateId,
      Long brandId,
      Long productId,
      Integer priceList,
      Integer priority,
      String startDate,
      String endDate,
      BigDecimal price,
      String currency) {

    super(aggregateId);
    this.brandId = brandId;
    this.productId = productId;
    this.priceList = priceList;
    this.priority = priority;
    this.startDate = startDate;
    this.endDate = endDate;
    this.price = price;
    this.currency = currency;
  }

  @Override
  public String eventName() {
    return "price.updated";
  }

  @Override
  public Map<String, Serializable> toPrimitives() {
    return Map.ofEntries(
        Map.entry("productId", productId),
        Map.entry("brandId", brandId),
        Map.entry("priceList", priceList),
        Map.entry("startDate", startDate),
        Map.entry("endDate", endDate),
        Map.entry("price", price),
        Map.entry("currency", currency));
  }
}
