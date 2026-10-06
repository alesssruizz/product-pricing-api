package com.inditex.pricing.prices.domain;

import java.util.List;
import java.util.Optional;

public interface PriceRepository {
  Optional<Price> findApplicablePrice(
      PriceBrandId brandId, PriceProductId productId, PriceDate applicationDate);

  Optional<Price> findById(PriceId id);

  List<Price> findAll();

  void create(Price price);

  void update(Price price);

  void deleteById(PriceId id);

  boolean existsConflict(Price price);

  boolean existsById(PriceId id);
}
