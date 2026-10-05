package com.inditex.pricing.prices.domain.exceptions;

import com.inditex.pricing.shared.domain.DomainError;

public final class PriceAlreadyExists extends DomainError {

  public PriceAlreadyExists() {
    super("A price already exists for those dates and priority", "price_already_exists");
  }
}
