package com.inditex.pricing.shared.domain;

import java.io.Serializable;
import java.util.UUID;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class Identifier implements Serializable {

  private static final int CANONICAL_UUID_LENGTH = 36;

  private final UUID value;

  protected Identifier(String value) {
    this.value = ensureValidUuid(value);
  }

  public String value() {
    return value.toString();
  }

  public UUID toUuid() {
    return value;
  }

  private static UUID ensureValidUuid(String value) {
    if (value == null || value.length() != CANONICAL_UUID_LENGTH) {
      throw new InvalidUUID(value);
    }
    try {
      return UUID.fromString(value);
    } catch (IllegalArgumentException ex) {
      throw new InvalidUUID(value);
    }
  }
}
