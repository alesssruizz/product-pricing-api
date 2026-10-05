package com.inditex.pricing.prices.application.update;

import java.math.BigDecimal;

public record UpdatePriceCommand(
    String id,
    Long brandId,
    Long productId,
    Integer priceList,
    Integer priority,
    String startDate,
    String endDate,
    BigDecimal price,
    String currency) {}
