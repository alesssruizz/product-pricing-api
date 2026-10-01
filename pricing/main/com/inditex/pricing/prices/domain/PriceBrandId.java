package com.inditex.pricing.prices.domain;

import com.inditex.pricing.shared.domain.LongValueObject;

public final class PriceBrandId extends LongValueObject {

  public PriceBrandId(Long value) {
    super(value);
  }

  public PriceBrandId() {
    super(0L);
  }
}
