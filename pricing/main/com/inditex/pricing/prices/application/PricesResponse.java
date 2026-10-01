package com.inditex.pricing.prices.application;

import java.util.List;

import com.inditex.pricing.shared.domain.bus.query.Response;

public record PricesResponse(List<PriceResponse> prices) implements Response {

  public PricesResponse {
    prices = List.copyOf(prices);
  }
}
