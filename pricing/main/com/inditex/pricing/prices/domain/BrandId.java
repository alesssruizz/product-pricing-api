package com.inditex.pricing.prices.domain;

import java.util.Objects;

import com.inditex.pricing.shared.domain.LongValueObject;

public final class BrandId extends LongValueObject {

  public BrandId(Long value) {
    super(Objects.requireNonNull(value));
  }
}
