package com.inditex.pricing.prices.domain;

import com.inditex.pricing.shared.domain.LongValueObject;

public final class PriceProductId extends LongValueObject {

    public PriceProductId(Long value) {
        super(value);
    }

    public PriceProductId() {
        super(0L);
    }
}
