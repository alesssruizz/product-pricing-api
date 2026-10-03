package com.inditex.pricing.prices.application.find;

import com.inditex.pricing.shared.domain.bus.query.Query;

public record FindApplicablePriceQuery(Long brandId, Long productId, String applicationDate)
    implements Query {}
