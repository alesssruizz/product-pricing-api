package com.inditex.pricing.prices.domain;

import static com.inditex.pricing.shared.domain.Required.ensureProvided;

import java.util.Currency;

import com.inditex.pricing.prices.domain.exception.InvalidPriceCurrency;
import com.inditex.pricing.shared.domain.StringValueObject;

public final class PriceCurrency extends StringValueObject {

  public PriceCurrency(String value) {
    super(ensureIsIso4217(ensureProvided(value, "currency")));
  }

  private static String ensureIsIso4217(String value) {
    try {
      Currency.getInstance(value);
    } catch (IllegalArgumentException ex) {
      throw new InvalidPriceCurrency(value);
    }
    return value;
  }
}
