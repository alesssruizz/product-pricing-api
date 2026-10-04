package com.inditex.pricing.prices.domain;

import java.math.BigDecimal;
import java.util.Objects;

import com.inditex.pricing.shared.domain.BigDecimalValueObject;

public final class PriceQuantity extends BigDecimalValueObject {

  public PriceQuantity(BigDecimal value) {
    super(Objects.requireNonNull(value));
  }
}
