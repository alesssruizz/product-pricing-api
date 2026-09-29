package com.inditex.pricing.prices.domain;

import com.inditex.pricing.shared.domain.IntValueObject;

public final class PricePriority extends IntValueObject {

    public PricePriority(Integer value) {
        super(value);
    }

    public PricePriority() {
        super(0);
    }
}
