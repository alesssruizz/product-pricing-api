package com.inditex.pricing.prices.domain.policy;

import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.exception.PriceAlreadyExists;
import com.inditex.pricing.shared.domain.Service;

@Service
public class PriceConflictPolicy {

  private final PriceRepository repository;

  public PriceConflictPolicy(PriceRepository repository) {
    this.repository = repository;
  }

  public void ensureNoConflict(Price price) {
    if (repository.existsConflict(price)) {
      throw new PriceAlreadyExists();
    }
  }
}
