package com.inditex.pricing.prices.domain;

import java.util.List;
import java.util.Optional;

public interface PriceRepository {
    Optional<Price> findApplicablePrice(
        PriceBrandId brandId,
        PriceProductId productId,
        PriceDate applicationDate
    );

    List<Price> searchAll();
}
