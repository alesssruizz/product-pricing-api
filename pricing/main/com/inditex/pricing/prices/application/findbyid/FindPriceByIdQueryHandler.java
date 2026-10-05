package com.inditex.pricing.prices.application.findbyid;

import com.inditex.pricing.prices.application.PriceResponse;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.query.QueryHandler;

@Service
public class FindPriceByIdQueryHandler implements QueryHandler<FindPriceByIdQuery, PriceResponse> {

  private final PriceByIdFinder finder;

  public FindPriceByIdQueryHandler(PriceByIdFinder finder) {
    this.finder = finder;
  }

  @Override
  public PriceResponse handle(FindPriceByIdQuery query) {
    return finder.find(new PriceId(query.id()));
  }
}
