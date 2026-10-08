package com.inditex.pricing.prices.application.patch;

import java.util.Objects;

import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceAmount;
import com.inditex.pricing.prices.domain.PriceCurrency;
import com.inditex.pricing.prices.domain.PriceDate;
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
public final class PricePatcher {

  private final PriceRepository repository;

  private final PriceIntegrityChecker integrityChecker;

  private final EventBus eventBus;

  public PricePatcher(
      PriceRepository repository, PriceIntegrityChecker integrityChecker, EventBus eventBus) {
    this.repository = repository;
    this.integrityChecker = integrityChecker;
    this.eventBus = eventBus;
  }

  public void patch(
      PriceId id,
      BrandId brandId,
      ProductId productId,
      PriceList priceList,
      PricePriority priority,
      PriceDate startDate,
      PriceDate endDate,
      PriceAmount price,
      PriceCurrency currency) {

    Price current = repository.findById(id).orElseThrow(() -> new PriceNotFoundException(id));

    var mergedBrandId = Objects.requireNonNullElse(brandId, current.brandId());
    var mergedProductId = Objects.requireNonNullElse(productId, current.productId());
    var mergedPriceList = Objects.requireNonNullElse(priceList, current.priceList());
    var mergedPriority = Objects.requireNonNullElse(priority, current.priority());
    var mergedStartDate = Objects.requireNonNullElse(startDate, current.startDate());
    var mergedEndDate = Objects.requireNonNullElse(endDate, current.endDate());
    var mergedPrice = Objects.requireNonNullElse(price, current.priceAmount());
    var mergedCurrency = Objects.requireNonNullElse(currency, current.currency());

    Price merged =
        Price.update(
            id,
            mergedBrandId,
            mergedStartDate,
            mergedEndDate,
            mergedPriceList,
            mergedProductId,
            mergedPriority,
            mergedPrice,
            mergedCurrency);

    integrityChecker.ensureCanBeSaved(merged);

    repository.update(merged);
    eventBus.publish(merged.pullDomainEvents());
  }
}
