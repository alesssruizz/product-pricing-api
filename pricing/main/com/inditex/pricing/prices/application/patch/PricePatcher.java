package com.inditex.pricing.prices.application.patch;

import java.util.Objects;

import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceIntegrityChecker;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.Service;

@Service
public final class PricePatcher {

  private final PriceRepository repository;

  private final PriceIntegrityChecker integrityChecker;

  public PricePatcher(PriceRepository repository, PriceIntegrityChecker integrityChecker) {
    this.repository = repository;
    this.integrityChecker = integrityChecker;
  }

  public void patch(PatchPriceCommand command) {
    var id = new PriceId(command.id());

    Price current = repository.findById(id).orElseThrow(() -> new PriceNotFoundException(id));

    Price price =
        Price.create(
            command.id(),
            Objects.requireNonNullElse(command.brandId(), current.brandId().value()),
            Objects.requireNonNullElse(command.productId(), current.productId().value()),
            Objects.requireNonNullElse(command.priceList(), current.priceList().value()),
            Objects.requireNonNullElse(command.priority(), current.priority().value()),
            Objects.requireNonNullElse(command.startDate(), current.startDate().value().toString()),
            Objects.requireNonNullElse(command.endDate(), current.endDate().value().toString()),
            Objects.requireNonNullElse(command.price(), current.priceAmount().value()),
            Objects.requireNonNullElse(command.currency(), current.currency().value()));

    integrityChecker.ensureCanBeSaved(price);

    repository.update(price);
  }
}
