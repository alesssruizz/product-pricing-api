package com.inditex.pricing.prices.domain;

import com.inditex.pricing.shared.domain.StringValueObject;

public class PriceCurrency extends StringValueObject {

    public PriceCurrency(String value) {
        super(value);
    }

    public PriceCurrency() {
        super("");
    }
}
