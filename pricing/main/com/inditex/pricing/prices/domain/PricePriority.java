package com.inditex.pricing.prices.domain;

import static com.inditex.pricing.shared.domain.Required.ensureProvided;

import com.inditex.pricing.shared.domain.IntValueObject;

public final class PricePriority extends IntValueObject {

  public PricePriority(Integer value) {
    super(ensureProvided(value, "priority"));
  }
}
