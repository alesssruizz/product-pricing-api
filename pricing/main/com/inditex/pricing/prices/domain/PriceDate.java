package com.inditex.pricing.prices.domain;

import static com.inditex.pricing.shared.domain.Required.ensureProvided;

import java.time.LocalDateTime;

import com.inditex.pricing.shared.domain.LocalDateTimeValueObject;

public final class PriceDate extends LocalDateTimeValueObject {

  public PriceDate(String value) {
    super(ensureProvided(value, "date"));
  }

  public PriceDate(LocalDateTime value) {
    super(ensureProvided(value, "date"));
  }
}
