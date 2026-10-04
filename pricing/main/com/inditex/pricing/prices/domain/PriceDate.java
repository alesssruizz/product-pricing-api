package com.inditex.pricing.prices.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import com.inditex.pricing.shared.domain.LocalDateTimeValueObject;

public final class PriceDate extends LocalDateTimeValueObject {

  public PriceDate(String value) {
    super(Objects.requireNonNull(value));
  }

  public PriceDate(LocalDateTime value) {
    super(Objects.requireNonNull(value));
  }
}
