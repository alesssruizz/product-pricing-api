package com.inditex.pricing.shared.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class IntValueObject {

  private final Integer value;

  public IntValueObject(Integer value) {
    this.value = value;
  }
}
