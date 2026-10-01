package com.inditex.pricing.prices.domain;

import com.inditex.pricing.shared.domain.IntValueObject;

public final class PriceList extends IntValueObject {

  public PriceList(Integer value) {
    super(value);
  }

  public PriceList() {
    super(0);
  }
}
