package com.inditex.pricing.prices.domain;

public interface PriceReferences {
  boolean brandExists(PriceBrandId id);

  boolean productExists(PriceProductId id);
}
