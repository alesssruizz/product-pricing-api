package com.inditex.pricing.prices.application.update;

import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceAmount;
import com.inditex.pricing.prices.domain.PriceCurrency;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceIntegrityChecker;
import com.inditex.pricing.prices.domain.PriceList;
import com.inditex.pricing.prices.domain.PricePriority;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.ProductId;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.event.EventBus;

@Service
public final class PriceUpdater {

  private final PriceRepository repository;

  private final PriceIntegrityChecker integrityChecker;

  private final EventBus eventBus;

  public PriceUpdater(
      PriceRepository repository, PriceIntegrityChecker integrityChecker, EventBus eventBus) {
    this.repository = repository;
    this.integrityChecker = integrityChecker;
    this.eventBus = eventBus;
  }

  public void update(
      PriceId id,
      BrandId brandId,
      ProductId productId,
      PriceList priceList,
      PricePriority priority,
      String startDate,
      String endDate,
      PriceAmount price,
      PriceCurrency currency) {
    if (!repository.existsById(id)) {
      throw new PriceNotFoundException(id);
    }

    Price updated =
        Price.update(
            id.value(),
            brandId.value(),
            productId.value(),
            priceList.value(),
            priority.value(),
            startDate,
            endDate,
            price.value(),
            currency.value());

    integrityChecker.ensureCanBeSaved(updated);

    repository.update(updated);
    eventBus.publish(updated.pullDomainEvents());
  }
}
