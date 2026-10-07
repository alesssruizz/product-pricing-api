package com.inditex.pricing.prices.domain;

import java.util.Objects;

import com.inditex.pricing.shared.domain.LongValueObject;

public final class ProductId extends LongValueObject {

  public ProductId(Long value) {
    super(Objects.requireNonNull(value));
  }
}
