package com.inditex.pricing.prices.domain;

import static com.inditex.pricing.shared.domain.Required.ensureProvided;

import com.inditex.pricing.shared.domain.IntValueObject;

public final class PriceList extends IntValueObject {

  public PriceList(Integer value) {
    super(ensureProvided(value, "priceList"));
  }
}
