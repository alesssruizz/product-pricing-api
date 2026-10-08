package com.inditex.pricing.prices.application.create;

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
import com.inditex.pricing.prices.domain.exceptions.PriceIdAlreadyExists;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.event.EventBus;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public final class PriceCreator {

  private final PriceRepository repository;

  private final PriceIntegrityChecker integrityChecker;

  private final EventBus eventBus;

  public void create(
      PriceId priceId,
      BrandId brandId,
      ProductId productId,
      PriceList priceList,
      PricePriority priority,
      String startDate,
      String endDate,
      PriceAmount priceAmount,
      PriceCurrency priceCurrency) {

    Price price =
        Price.create(
            priceId.value(),
            brandId.value(),
            productId.value(),
            priceList.value(),
            priority.value(),
            startDate,
            endDate,
            priceAmount.value(),
            priceCurrency.value());

    if (repository.existsById(priceId)) {
      throw new PriceIdAlreadyExists(priceId);
    }
    integrityChecker.ensureCanBeSaved(price);

    repository.create(price);
    eventBus.publish(price.pullDomainEvents());
  }
}
