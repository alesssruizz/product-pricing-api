package com.inditex.pricing.prices.domain;

import java.util.Objects;

import com.inditex.pricing.shared.domain.LongValueObject;

public final class PriceBrandId extends LongValueObject {

  public PriceBrandId(Long value) {
    super(Objects.requireNonNull(value));
  }
}
