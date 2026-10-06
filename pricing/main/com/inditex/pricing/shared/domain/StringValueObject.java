package com.inditex.pricing.shared.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class StringValueObject {

  private final String value;

  protected StringValueObject(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value();
  }
}
