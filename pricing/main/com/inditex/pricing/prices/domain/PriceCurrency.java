package com.inditex.pricing.prices.domain;

import java.util.Currency;

import com.inditex.pricing.prices.domain.exceptions.InvalidPriceCurrency;
import com.inditex.pricing.shared.domain.StringValueObject;
import com.inditex.pricing.shared.domain.exception.FieldRequired;

public final class PriceCurrency extends StringValueObject {

  public PriceCurrency(String value) {
    super(ensureIsIso4217(value));
  }

  private static String ensureIsIso4217(String value) {
    if (value == null || value.isBlank()) {
      throw new FieldRequired("currency");
    }
    try {
      Currency.getInstance(value);
    } catch (IllegalArgumentException ex) {
      throw new InvalidPriceCurrency(value);
    }
    return value;
  }
}
