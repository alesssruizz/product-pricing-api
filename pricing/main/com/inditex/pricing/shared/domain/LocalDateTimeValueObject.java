package com.inditex.pricing.shared.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.inditex.pricing.shared.domain.exception.InvalidDateFormat;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class LocalDateTimeValueObject {

  private final LocalDateTime value;

  protected LocalDateTimeValueObject(String value) {
    this.value = ensureIsValidLocalDateTime(value);
  }

  protected LocalDateTimeValueObject(LocalDateTime value) {
    this.value = value;
  }

  private LocalDateTime ensureIsValidLocalDateTime(String value) throws InvalidDateFormat {
    try {
      return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    } catch (DateTimeParseException ex) {
      throw new InvalidDateFormat(value);
    }
  }
}
