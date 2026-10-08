package com.inditex.pricing.shared.domain.exception;

public final class InvalidDateFormat extends DomainError {

  public InvalidDateFormat(String value) {
    super(
        String.format("Date <%s> is not a valid format. Example: 2020-06-14T10:00:00", value),
        "invalid_date_format");
  }
}
