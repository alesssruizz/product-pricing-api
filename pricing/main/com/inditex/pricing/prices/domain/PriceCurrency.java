package com.inditex.pricing.prices.domain;

import java.util.Currency;
import java.util.Objects;

import com.inditex.pricing.prices.domain.exceptions.InvalidPriceCurrency;
import com.inditex.pricing.shared.domain.StringValueObject;

public final class PriceCurrency extends StringValueObject {

  public PriceCurrency(String value) {
    super(ensureIsIso4217(value));
  }

  private static String ensureIsIso4217(String value) {
    Objects.requireNonNull(value);
    try {
      Currency.getInstance(value);
    } catch (IllegalArgumentException ex) {
      throw new InvalidPriceCurrency(value);
    }
    return value;
  }
}
