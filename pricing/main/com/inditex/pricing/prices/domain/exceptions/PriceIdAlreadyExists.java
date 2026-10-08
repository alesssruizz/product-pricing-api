package com.inditex.pricing.prices.domain.exceptions;

import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.shared.domain.exception.DomainError;

public final class PriceIdAlreadyExists extends DomainError {

  public PriceIdAlreadyExists(PriceId id) {
    super(
        String.format("A price with id <%s> already exists", id.value()),
        "price_id_already_exists");
  }
}
