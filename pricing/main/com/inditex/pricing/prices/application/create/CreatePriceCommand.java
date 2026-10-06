package com.inditex.pricing.prices.application.create;

import java.math.BigDecimal;

import com.inditex.pricing.shared.domain.bus.command.Command;

public record CreatePriceCommand(
    String id,
    Long brandId,
    Long productId,
    Integer priceList,
    Integer priority,
    String startDate,
    String endDate,
    BigDecimal price,
    String currency)
    implements Command {}
