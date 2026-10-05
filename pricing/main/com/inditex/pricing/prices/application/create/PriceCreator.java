package com.inditex.pricing.prices.application.create;

import com.inditex.pricing.prices.application.PriceResponse;
import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceIntegrityChecker;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.exceptions.PriceIdAlreadyExists;
import com.inditex.pricing.shared.domain.Service;

@Service
public final class PriceCreator {

  private final PriceRepository repository;

  private final PriceIntegrityChecker integrityChecker;

  public PriceCreator(PriceRepository repository, PriceIntegrityChecker integrityChecker) {
    this.repository = repository;
    this.integrityChecker = integrityChecker;
  }

  public PriceResponse create(CreatePriceCommand command) {
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

    PriceId id = price.id();
    if (repository.existsById(id)) {
      throw new PriceIdAlreadyExists(id);
    }
    integrityChecker.ensureCanBeSaved(price);

    return PriceResponse.fromAggregate(repository.save(price));
  }
}
