package com.inditex.pricing.shared.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class LongValueObject {

  private final Long value;

  public LongValueObject(Long value) {
    this.value = value;
  }
}
