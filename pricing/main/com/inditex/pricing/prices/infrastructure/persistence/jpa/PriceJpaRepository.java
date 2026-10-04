package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;

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

import org.springframework.data.domain.Limit;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PriceJpaRepository implements PriceRepository {

  private final SpringDataPriceRepository repository;

  public PriceJpaRepository(SpringDataPriceRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<Price> findApplicablePrice(
      PriceBrandId brandId, PriceProductId productId, PriceDate applicationDate) {
    return repository
        .findApplicablePrice(
            brandId.value(), productId.value(), applicationDate.value(), Limit.of(1))
        .stream()
        .findFirst()
        .map(this::toDomain);
  }

  @Override
  public List<Price> findAll() {
    return repository.findAll().stream().map(this::toDomain).toList();
  }

  private Price toDomain(PriceJpaEntity entity) {
    return Price.builder()
		.brandId(new PriceBrandId(entity.getBrandId()))
		.startDate(new PriceDate(entity.getStartDate()))
		.endDate(new PriceDate(entity.getEndDate()))
		.priceList(new PriceList(entity.getPriceList()))
		.productId(new PriceProductId(entity.getProductId()))
		.priority(new PricePriority(entity.getPriority()))
		.priceQuantity(new PriceQuantity(entity.getPrice()))
		.currency(new PriceCurrency(entity.getCurrency()))
		.build();
  }
}
