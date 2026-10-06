package com.inditex.pricing.prices.domain.exceptions;

import com.inditex.pricing.prices.domain.PriceBrandId;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceProductId;
import com.inditex.pricing.shared.domain.DomainError;

public final class PriceNotFoundException extends DomainError {

  public PriceNotFoundException(
      PriceBrandId brandId, PriceProductId productId, PriceDate applicationDate) {
    super(
        String.format(
            "Price not found for brandId <%s>, productId <%s> and application date <%s>",
            brandId.value(), productId.value(), applicationDate.value()),
        "price_not_found");
  }

  public PriceNotFoundException(PriceId id) {
    super(String.format("Price not found for id <%s>", id.value()), "price_not_found");
  }
}
