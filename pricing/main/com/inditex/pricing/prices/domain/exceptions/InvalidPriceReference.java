package com.inditex.pricing.prices.domain.exceptions;

import com.inditex.pricing.shared.domain.exception.DomainError;

public final class InvalidPriceReference extends DomainError {

  public InvalidPriceReference(String kind, Long id) {
    super(String.format("%s %s does not exist", kind, id), "invalid_reference");
  }
}
