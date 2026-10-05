package com.inditex.pricing.prices.application.findbyid;

import com.inditex.pricing.prices.application.PriceResponse;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.Service;

@Service
public final class PriceByIdFinder {

  private final PriceRepository repository;

  public PriceByIdFinder(PriceRepository repository) {
    this.repository = repository;
  }

  public PriceResponse find(PriceId id) {
    return repository
        .findById(id)
        .map(PriceResponse::fromAggregate)
        .orElseThrow(() -> new PriceNotFoundException(id));
  }
}
