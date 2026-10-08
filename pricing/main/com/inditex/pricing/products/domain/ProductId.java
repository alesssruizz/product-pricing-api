package com.inditex.pricing.products.domain;

import static com.inditex.pricing.shared.domain.Required.ensureProvided;

import com.inditex.pricing.shared.domain.LongValueObject;

public final class ProductId extends LongValueObject {

  public ProductId(Long value) {
    super(ensureProvided(value, "productId"));
  }
}
