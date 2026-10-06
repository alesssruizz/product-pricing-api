package com.inditex.pricing.shared.domain;

import java.math.BigDecimal;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class BigDecimalValueObject {

  private final BigDecimal value;

  protected BigDecimalValueObject(BigDecimal value) {
    this.value = value;
  }
}
