package com.inditex.pricing.prices.domain;

import com.inditex.pricing.shared.domain.Identifier;
import com.inditex.pricing.shared.domain.exception.FieldRequired;

public final class PriceId extends Identifier {

  public PriceId(String value) {
    super(ensureProvided(value));
  }

  private static String ensureProvided(String value) {
    if (value == null || value.isBlank()) {
      throw new FieldRequired("id");
    }
    return value;
  }
}
