package com.inditex.pricing.prices.domain;

public interface PriceReferences {
  boolean brandExists(BrandId id);

  boolean productExists(ProductId id);
}
