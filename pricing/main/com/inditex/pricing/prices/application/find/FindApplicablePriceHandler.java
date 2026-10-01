package com.inditex.pricing.prices.application.find;

import com.inditex.pricing.prices.application.PriceResponse;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.query.QueryHandler;

@Service
public class FindApplicablePriceHandler
    implements QueryHandler<FindApplicablePriceQuery, PriceResponse> {

  private final PriceFinder finder;

  public FindApplicablePriceHandler(PriceFinder finder) {
    this.finder = finder;
  }

  @Override
  public PriceResponse handle(FindApplicablePriceQuery query) {
    return finder.find(query);
  }
}
