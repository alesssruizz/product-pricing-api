package com.inditex.pricing.prices.application.findbyid;

import com.inditex.pricing.shared.domain.bus.query.Query;

public record FindPriceByIdQuery(String id) implements Query {}
