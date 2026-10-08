package com.inditex.pricing.prices.application.find;

import com.inditex.pricing.prices.application.ApplicablePriceResponse;
import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.ProductId;
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
    var brandId = new BrandId(query.brandId());
    var productId = new ProductId(query.productId());
    var applicationDate = new PriceDate(query.applicationDate());

    return finder.find(brandId, productId, applicationDate);
  }
}
