package com.inditex.pricing.prices.application.update;

import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceIntegrityChecker;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.Service;

@Service
public final class PriceUpdater {

  private final PriceRepository repository;

  private final PriceIntegrityChecker integrityChecker;

  public PriceUpdater(PriceRepository repository, PriceIntegrityChecker integrityChecker) {
    this.repository = repository;
    this.integrityChecker = integrityChecker;
  }

  public void update(UpdatePriceCommand command) {
    var id = new PriceId(command.id());

    if (!repository.existsById(id)) {
      throw new PriceNotFoundException(id);
    }

    Price price =
        Price.create(
            command.id(),
            command.brandId(),
            command.productId(),
            command.priceList(),
            command.priority(),
            command.startDate(),
            command.endDate(),
            command.price(),
            command.currency());

    integrityChecker.ensureCanBeSaved(price);

    repository.update(price);
  }
}
