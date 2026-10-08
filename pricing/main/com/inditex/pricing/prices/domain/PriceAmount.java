package com.inditex.pricing.prices.domain;

import static com.inditex.pricing.shared.domain.Required.ensureProvided;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.exceptions.InvalidPriceAmount;
import com.inditex.pricing.shared.domain.BigDecimalValueObject;

public final class PriceAmount extends BigDecimalValueObject {

  public PriceAmount(BigDecimal value) {
    super(ensureIsPositive(ensureProvided(value, "price")));
  }

  private static BigDecimal ensureIsPositive(BigDecimal value) {
    if (value.signum() <= 0) {
      throw new InvalidPriceAmount();
    }
    return value;
  }
}
