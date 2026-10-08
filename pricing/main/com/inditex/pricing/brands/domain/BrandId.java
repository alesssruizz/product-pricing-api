package com.inditex.pricing.brands.domain;

import static com.inditex.pricing.shared.domain.Required.ensureProvided;

import com.inditex.pricing.shared.domain.LongValueObject;

public final class BrandId extends LongValueObject {

  public BrandId(Long value) {
    super(ensureProvided(value, "brandId"));
  }
}
