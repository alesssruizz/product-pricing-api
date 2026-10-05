package com.inditex.pricing.prices.application.find;

import com.inditex.pricing.prices.application.ApplicablePriceResponse;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.query.QueryHandler;

@Service
public class FindApplicablePriceHandler
    implements QueryHandler<FindApplicablePriceQuery, ApplicablePriceResponse> {

  private final PriceFinder finder;

  public FindApplicablePriceHandler(PriceFinder finder) {
    this.finder = finder;
  }

  @Override
  public ApplicablePriceResponse handle(FindApplicablePriceQuery query) {
    return finder.find(query);
  }
}
