package com.inditex.pricing.prices.domain.exception;

import com.inditex.pricing.shared.domain.exception.DomainError;

public final class InvalidPriceCurrency extends DomainError {

  public InvalidPriceCurrency(String currency) {
    super(
        String.format("<%s> is not a valid ISO 4217 currency code", currency),
        "invalid_price_currency");
  }
}
