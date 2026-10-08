package com.inditex.pricing.prices.domain.exceptions;

import com.inditex.pricing.shared.domain.exception.DomainError;

public final class InvalidPriceAmount extends DomainError {

  public InvalidPriceAmount() {
    super("price_amount must be greater than 0", "invalid_price_amount");
  }
}
