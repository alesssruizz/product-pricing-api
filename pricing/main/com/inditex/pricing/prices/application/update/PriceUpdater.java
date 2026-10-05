package com.inditex.pricing.prices.application.update;

import com.inditex.pricing.prices.application.PriceResponse;
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

  public PriceResponse update(UpdatePriceCommand command) {
    PriceId id = new PriceId(command.id());

    repository.findById(id).orElseThrow(() -> new PriceNotFoundException(id));

    Price price =
        Price.create(
                command.brandId(),
                command.productId(),
                command.priceList(),
                command.priority(),
                command.startDate(),
                command.endDate(),
                command.price(),
                command.currency())
            .withId(id);

    integrityChecker.ensureCanBeSaved(price);

    return PriceResponse.fromAggregate(repository.save(price));
  }
}
