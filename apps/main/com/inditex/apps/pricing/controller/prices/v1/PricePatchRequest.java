package com.inditex.apps.pricing.controller.prices.v1;

import java.math.BigDecimal;

public record PricePatchRequest(
    Long brandId,
    Long productId,
    Integer priceList,
    Integer priority,
    String startDate,
    String endDate,
    BigDecimal price,
    String currency) {}
