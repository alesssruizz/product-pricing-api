package com.inditex.pricing.prices.application.find;

import com.inditex.pricing.shared.domain.bus.query.Query;

public final class FindApplicablePriceQuery implements Query {

  private final Long brandId;

  private final Long productId;

  private final String applicationDate;

  public FindApplicablePriceQuery(Long brandId, Long productId, String applicationDate) {
    this.brandId = brandId;
    this.productId = productId;
    this.applicationDate = applicationDate;
  }

  public Long brandId() {
    return brandId;
  }

  public Long productId() {
    return productId;
  }

  public String applicationDate() {
    return applicationDate;
  }
}
