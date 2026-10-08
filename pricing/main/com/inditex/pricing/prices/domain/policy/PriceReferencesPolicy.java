package com.inditex.pricing.prices.domain.policy;

import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceReferences;
import com.inditex.pricing.prices.domain.exception.InvalidPriceReference;
import com.inditex.pricing.shared.domain.Service;

@Service
public class PriceReferencesPolicy implements PricePolicy {

  private static final int ORDER = 10;

  private final PriceReferences references;

  public PriceReferencesPolicy(PriceReferences references) {
    this.references = references;
  }

  @Override
  public void ensure(Price price) {
    if (!references.brandExists(price.brandId())) {
      throw new InvalidPriceReference("brand", price.brandId().value());
    }
    if (!references.productExists(price.productId())) {
      throw new InvalidPriceReference("product", price.productId().value());
    }
  }

  @Override
  public int order() {
    return ORDER;
  }
}
