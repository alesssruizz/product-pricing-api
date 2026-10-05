package com.inditex.pricing.prices.application.patch;

import java.math.BigDecimal;

public record PatchPriceCommand(
    Long id,
    Long brandId,
    Long productId,
    Integer priceList,
    Integer priority,
    String startDate,
    String endDate,
    BigDecimal price,
    String currency) {}
