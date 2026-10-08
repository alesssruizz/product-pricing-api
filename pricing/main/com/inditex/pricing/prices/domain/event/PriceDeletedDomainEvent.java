package com.inditex.pricing.prices.domain.event;

import java.io.Serializable;
import java.util.Map;

import com.inditex.pricing.shared.domain.bus.event.DomainEvent;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false)
@Getter
public class PriceDeletedDomainEvent extends DomainEvent {

  public PriceDeletedDomainEvent(String aggregateId) {
    super(aggregateId);
  }

  @Override
  public String eventName() {
    return "price.deleted";
  }

  @Override
  public Map<String, Serializable> toPrimitives() {
    return Map.of("priceId", this.aggregateId());
  }
}
