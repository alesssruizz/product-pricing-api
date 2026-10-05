package com.inditex.pricing.prices.application.find;

import com.inditex.pricing.prices.application.ApplicablePriceResponse;
import com.inditex.pricing.prices.domain.PriceBrandId;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceProductId;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.Service;

@Service
public final class PriceFinder {

  private final PriceRepository repository;

  public PriceFinder(PriceRepository repository) {
    this.repository = repository;
  }

  public ApplicablePriceResponse find(FindApplicablePriceQuery query) {
    PriceBrandId brandId = new PriceBrandId(query.brandId());
    PriceProductId productId = new PriceProductId(query.productId());
    PriceDate applicationDate = new PriceDate(query.applicationDate());

    return repository
        .findApplicablePrice(brandId, productId, applicationDate)
        .map(ApplicablePriceResponse::fromAggregate)
        .orElseThrow(() -> new PriceNotFoundException(brandId, productId, applicationDate));
  }
}
