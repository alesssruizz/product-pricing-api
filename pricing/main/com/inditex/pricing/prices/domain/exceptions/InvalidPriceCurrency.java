package com.inditex.pricing.prices.domain.exceptions;

import com.inditex.pricing.shared.domain.DomainError;

public final class InvalidPriceCurrency extends DomainError {

  public InvalidPriceCurrency(String currency) {
    super(
        String.format("<%s> is not a valid ISO 4217 currency code", currency),
        "invalid_price_currency");
  }
}
