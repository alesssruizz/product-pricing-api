package com.inditex.pricing.shared.domain;

import com.inditex.pricing.shared.domain.exception.FieldRequired;

public final class Required {

  public static <T> T ensureProvided(T value, String fieldName) {
    if (value == null || (value instanceof String str && str.isBlank())) {
      throw new FieldRequired(fieldName);
    }
    return value;
  }
}
