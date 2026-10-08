package com.inditex.pricing.prices.domain.exception;

import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.ProductId;
import com.inditex.pricing.shared.domain.exception.DomainError;

public final class PriceNotFoundException extends DomainError {

  public PriceNotFoundException(BrandId brandId, ProductId productId, PriceDate applicationDate) {
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
