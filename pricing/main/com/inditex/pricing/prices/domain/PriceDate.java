package com.inditex.pricing.prices.domain;

import java.time.LocalDateTime;

import com.inditex.pricing.shared.domain.LocalDateTimeValueObject;

public final class PriceDate extends LocalDateTimeValueObject {

    public PriceDate(String value) {
        super(value);
    }

    public PriceDate(LocalDateTime value) {
        super(value);
    }
}
