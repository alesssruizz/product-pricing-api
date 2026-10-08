package com.inditex.pricing.prices.application.delete;

import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.event.EventBus;

@Service
public final class PriceDeleter {

  private final PriceRepository repository;

  private final EventBus eventBus;

  public PriceDeleter(PriceRepository repository, EventBus eventBus) {
    this.repository = repository;
    this.eventBus = eventBus;
  }

  public void delete(PriceId id) {
    if (!repository.existsById(id)) {
      throw new PriceNotFoundException(id);
    }

    var price = Price.delete(id.value());

    repository.deleteById(id);
    eventBus.publish(price.pullDomainEvents());
  }
}
