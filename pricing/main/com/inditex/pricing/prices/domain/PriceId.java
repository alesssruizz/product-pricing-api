package com.inditex.pricing.prices.domain;

import static com.inditex.pricing.shared.domain.Required.ensureProvided;

import com.inditex.pricing.shared.domain.Identifier;

public final class PriceId extends Identifier {

  public PriceId(String value) {
    super(ensureProvided(value, "id"));
  }
}
