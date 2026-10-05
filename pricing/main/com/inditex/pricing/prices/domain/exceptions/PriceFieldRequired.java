package com.inditex.pricing.prices.domain.exceptions;

import com.inditex.pricing.shared.domain.DomainError;

public final class PriceFieldRequired extends DomainError {

  public PriceFieldRequired(String field) {
    super(String.format("%s is required", field), "price_field_required");
  }
}
