package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceBrandId;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceProductId;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.shared.domain.Service;

import org.springframework.data.domain.Limit;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PriceJpaAdapter implements PriceRepository {

  private final PriceJpaRepository repository;

  public PriceJpaAdapter(PriceJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<Price> findApplicablePrice(
      PriceBrandId brandId, PriceProductId productId, PriceDate applicationDate) {
    return repository
        .findApplicablePrice(
            brandId.value(), productId.value(), applicationDate.value())
        .stream()
        .findFirst()
        .map(this::toDomain);
  }

  @Override
  public Optional<Price> findById(PriceId id) {
    return repository.findById(id.toUuid()).map(this::toDomain);
  }

  @Override
  public List<Price> findAll() {
    return repository.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  @Transactional
  public void create(Price price) {
    repository.save(PriceJpaEntity.forCreate(price));
  }

  @Override
  @Transactional
  public void update(Price price) {
    repository.save(PriceJpaEntity.forUpdate(price));
  }

  @Override
  @Transactional
  public void deleteById(PriceId id) {
    repository.deleteById(id.toUuid());
  }

  @Override
  public boolean existsConflict(Price price) {
    return repository.countConflicts(
            price.brandId().value(),
            price.productId().value(),
            price.priority().value(),
            price.startDate().value(),
            price.id().toUuid())
        > 0;
  }

  @Override
  public boolean existsById(PriceId id) {
    return repository.existsById(id.toUuid());
  }

  private Price toDomain(PriceJpaEntity entity) {
    return new Price(
        Objects.requireNonNull(entity.getId()).toString(),
        entity.getBrandId(),
        entity.getProductId(),
        entity.getPriceList(),
        entity.getPriority(),
        entity.getStartDate().toString(),
        entity.getEndDate().toString(),
        entity.getPrice(),
        entity.getCurrency());
  }
}
