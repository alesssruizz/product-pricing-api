package com.inditex.pricing.prices.domain;

import java.util.Objects;

import com.inditex.pricing.shared.domain.IntValueObject;

public final class PricePriority extends IntValueObject {

  public PricePriority(Integer value) {
    super(Objects.requireNonNull(value));
  }
}
