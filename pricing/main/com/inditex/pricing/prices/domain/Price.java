package com.inditex.pricing.prices.domain;

import lombok.Builder;

@Builder
public record Price(
    PriceBrandId brandId,
    PriceDate startDate,
    PriceDate endDate,
    PriceList priceList,
    PriceProductId productId,
    PricePriority priority,
    PriceQuantity priceQuantity,
    PriceCurrency currency) {}
