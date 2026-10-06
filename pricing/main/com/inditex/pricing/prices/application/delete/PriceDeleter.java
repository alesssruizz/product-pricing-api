package com.inditex.pricing.prices.application.delete;

import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.Service;

@Service
public final class PriceDeleter {

  private final PriceRepository repository;

  public PriceDeleter(PriceRepository repository) {
    this.repository = repository;
  }

  public void delete(String id) {
    var priceId = new PriceId(id);

    if (!repository.existsById(priceId)) {
      throw new PriceNotFoundException(priceId);
    }

    repository.deleteById(priceId);
  }
}
