package com.inditex.pricing.prices.domain;

import java.util.Objects;

public record Price(
    PriceBrandId brandId,
    PriceDate startDate,
    PriceDate endDate,
    PriceList priceList,
    PriceProductId productId,
    PricePriority priority,
    PriceQuantity priceQuantity,
    PriceCurrency currency
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return (
            Objects.equals(endDate, price.endDate) &&
            Objects.equals(startDate, price.startDate) &&
            Objects.equals(priceList, price.priceList) &&
            Objects.equals(brandId, price.brandId) &&
            Objects.equals(priority, price.priority) &&
            Objects.equals(currency, price.currency) &&
            Objects.equals(productId, price.productId) &&
            Objects.equals(priceQuantity, price.priceQuantity)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            brandId,
            startDate,
            endDate,
            priceList,
            productId,
            priority,
            priceQuantity,
            currency
        );
    }
}
