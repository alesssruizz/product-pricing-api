package com.inditex.pricing.shared.domain;

public class InvalidDateFormat extends DomainError {

    public InvalidDateFormat(String value) {
        super(
            String.format(
                "La fecha <%s> no tiene un formato válido. Ej: 2020-06-14T10:00:00",
                value
            ),
            "invalid_date_format"
        );
    }
}
