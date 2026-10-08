package com.inditex.pricing.shared.domain.exception;

public final class InvalidUUID extends DomainError {

  public InvalidUUID(String value) {
    super(String.format("The value <%s> is not a valid UUID", value), "invalid_uuid");
  }
}
