package com.inditex.pricing.prices.domain;

import com.inditex.pricing.shared.domain.IntValueObject;
import com.inditex.pricing.shared.domain.exception.FieldRequired;

public final class PricePriority extends IntValueObject {

  public PricePriority(Integer value) {
    super(ensureProvided(value));
  }

  private static Integer ensureProvided(Integer value) {
    if (value == null) {
      throw new FieldRequired("priority");
    }
    return value;
  }
}
