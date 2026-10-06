package com.inditex.pricing.prices.application.update;

import java.math.BigDecimal;

import com.inditex.pricing.shared.domain.bus.command.Command;

public record UpdatePriceCommand(
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
