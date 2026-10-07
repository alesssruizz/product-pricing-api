package com.inditex.pricing.prices.domain;

import java.math.BigDecimal;
import java.util.Objects;

import com.inditex.pricing.prices.domain.exceptions.InvalidPriceAmount;
import com.inditex.pricing.shared.domain.BigDecimalValueObject;

public final class PriceAmount extends BigDecimalValueObject {

  public PriceAmount(BigDecimal value) {
    super(ensureIsPositive(value));
  }

  private static BigDecimal ensureIsPositive(BigDecimal value) {
    if (Objects.requireNonNull(value).signum() <= 0) {
      throw new InvalidPriceAmount();
    }
    return value;
  }
}
