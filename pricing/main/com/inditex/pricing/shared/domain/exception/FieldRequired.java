package com.inditex.pricing.shared.domain.exception;

public final class FieldRequired extends DomainError {

  public FieldRequired(String field) {
    super(String.format("%s is required", field), "field_required");
  }
}
