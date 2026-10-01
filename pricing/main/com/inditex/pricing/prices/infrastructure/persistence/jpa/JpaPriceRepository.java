package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceBrandId;
import com.inditex.pricing.prices.domain.PriceCurrency;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceList;
import com.inditex.pricing.prices.domain.PricePriority;
import com.inditex.pricing.prices.domain.PriceProductId;
import com.inditex.pricing.prices.domain.PriceQuantity;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.shared.domain.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class JpaPriceRepository implements PriceRepository {

  private final SpringDataPriceRepository jpaRepository;

  public JpaPriceRepository(SpringDataPriceRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Optional<Price> findApplicablePrice(
      PriceBrandId brandId, PriceProductId productId, PriceDate applicationDate) {
    List<Price> candidates =
        jpaRepository
            .findApplicablePrice(brandId.value(), productId.value(), applicationDate.value())
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());

    return Price.mostApplicable(candidates);
  }

  @Override
  public List<Price> searchAll() {
    return jpaRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
  }

  private Price toDomain(PriceJpaEntity entity) {
    return new Price(
        new PriceBrandId(entity.brandId()),
        new PriceDate(entity.startDate()),
        new PriceDate(entity.endDate()),
        new PriceList(entity.priceList()),
        new PriceProductId(entity.productId()),
        new PricePriority(entity.priority()),
        new PriceQuantity(entity.price()),
        new PriceCurrency(entity.currency()));
  }
}
