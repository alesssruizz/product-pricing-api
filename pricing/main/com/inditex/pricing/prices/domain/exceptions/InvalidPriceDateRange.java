package com.inditex.pricing.prices.domain.exceptions;

import com.inditex.pricing.shared.domain.DomainError;

public final class InvalidPriceDateRange extends DomainError {

  public InvalidPriceDateRange() {
    super("endDate must be after startDate", "invalid_price_date_range");
  }
}
