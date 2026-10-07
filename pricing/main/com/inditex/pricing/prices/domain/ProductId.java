package com.inditex.pricing.prices.domain;

import java.util.Objects;

import com.inditex.pricing.shared.domain.LongValueObject;

public final class PriceProductId extends LongValueObject {

  public PriceProductId(Long value) {
    super(Objects.requireNonNull(value));
  }
}
