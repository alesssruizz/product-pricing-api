package com.inditex.pricing.prices.domain;

import java.util.Objects;

import com.inditex.pricing.shared.domain.StringValueObject;

public final class PriceCurrency extends StringValueObject {

  public PriceCurrency(String value) {
    super(Objects.requireNonNull(value));
  }
}
