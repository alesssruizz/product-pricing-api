package com.inditex.pricing.prices.domain;

import com.inditex.pricing.shared.domain.DomainError;

public final class PriceNotFoundException extends DomainError {

    public PriceNotFoundException(
        PriceBrandId brandId,
        PriceProductId productId,
        PriceDate applicationDate
    ) {
        super(
            String.format(
                "Precio no encontrado para la brandId <%s>, el productId <%s> y la fecha de aplicación <%s>",
                brandId.value(),
                productId.value(),
                applicationDate.value()
            ),
            "price_not_found"
        );
    }
}
