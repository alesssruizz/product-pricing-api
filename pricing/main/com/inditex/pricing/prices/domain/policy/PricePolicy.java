package com.inditex.pricing.prices.domain.policy;

import com.inditex.pricing.prices.domain.Price;

public interface PricePolicy {

  void ensure(Price price);

  int order();
}
