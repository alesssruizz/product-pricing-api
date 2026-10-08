package com.inditex.pricing.prices.domain;

import com.inditex.pricing.prices.domain.exception.InvalidPriceReference;
import com.inditex.pricing.prices.domain.exception.PriceAlreadyExists;
import com.inditex.pricing.shared.domain.Service;

@Service
public class PriceIntegrityChecker {

  private final PriceRepository repository;

  private final PriceReferences references;

  public PriceIntegrityChecker(PriceRepository repository, PriceReferences references) {
    this.repository = repository;
    this.references = references;
  }

  public void ensureCanBeSaved(Price price) {
    if (!references.brandExists(price.brandId())) {
      throw new InvalidPriceReference("brand", price.brandId().value());
    }
    if (!references.productExists(price.productId())) {
      throw new InvalidPriceReference("product", price.productId().value());
    }
    if (repository.existsConflict(price)) {
      throw new PriceAlreadyExists();
    }
  }
}
