package com.inditex.pricing.prices.domain.exceptions;

import com.inditex.pricing.shared.domain.DomainError;

public final class InvalidPriceQuantity extends DomainError {

  public InvalidPriceQuantity() {
    super("priceQuantity must be greater than 0", "invalid_price_quantity");
  }
}
