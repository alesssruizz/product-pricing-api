package com.inditex.pricing.prices.domain;

import java.util.Objects;

import com.inditex.pricing.shared.domain.IntValueObject;

public final class PriceList extends IntValueObject {

  public PriceList(Integer value) {
    super(Objects.requireNonNull(value));
  }
}
