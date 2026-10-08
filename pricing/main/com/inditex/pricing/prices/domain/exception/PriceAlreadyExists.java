package com.inditex.pricing.prices.domain.exception;

import com.inditex.pricing.shared.domain.exception.DomainError;

public final class PriceAlreadyExists extends DomainError {

  public PriceAlreadyExists() {
    super("A price already exists for those dates and priority", "price_already_exists");
  }
}
