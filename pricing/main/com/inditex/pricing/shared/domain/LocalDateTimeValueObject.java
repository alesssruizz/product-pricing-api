package com.inditex.pricing.shared.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public abstract class LocalDateTimeValueObject {

    private LocalDateTime value;

    public LocalDateTimeValueObject(String value) {
        this.value = ensureIsValidLocalDateTime(value);
    }

    public LocalDateTimeValueObject(LocalDateTime value) {
        this.value = Objects.requireNonNull(value);
    }

    private LocalDateTime ensureIsValidLocalDateTime(String value) throws InvalidDateFormat {
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException ex) {
            throw new InvalidDateFormat(value);
        }
    }

    public LocalDateTime value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalDateTimeValueObject that = (LocalDateTimeValueObject) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
