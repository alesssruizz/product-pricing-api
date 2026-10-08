package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.inditex.pricing.brands.infrastructure.persistence.jpa.BrandJpaRepository;
import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.ProductId;
import com.inditex.pricing.products.infrastructure.persistence.jpa.ProductJpaRepository;
import com.inditex.pricing.shared.domain.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PriceJpaAdapter implements PriceRepository {

  private final PriceJpaRepository repository;

  private final BrandJpaRepository brands;

  private final ProductJpaRepository products;

  public PriceJpaAdapter(
      PriceJpaRepository repository, BrandJpaRepository brands, ProductJpaRepository products) {
    this.repository = repository;
    this.brands = brands;
    this.products = products;
  }

  @Override
  public Optional<Price> findApplicablePrice(
      BrandId brandId, ProductId productId, PriceDate applicationDate) {
    return repository
        .findApplicablePrice(brandId.value(), productId.value(), applicationDate.value())
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
    repository.save(
        PriceJpaEntity.forCreate(
            price,
            brands.getReferenceById(price.brandId().value()),
            products.getReferenceById(price.productId().value())));
  }

  @Override
  @Transactional
  public void update(Price price) {
    repository.save(
        PriceJpaEntity.forUpdate(
            price,
            brands.getReferenceById(price.brandId().value()),
            products.getReferenceById(price.productId().value())));
  }

  @Override
  @Transactional
  public void deleteById(PriceId id) {
    repository.deleteById(id.toUuid());
  }

  @Override
  public boolean existsConflict(Price price) {
    return repository.existsByBrand_IdAndProduct_IdAndPriorityAndStartDateAndIdNot(
        price.brandId().value(),
        price.productId().value(),
        price.priority().value(),
        price.startDate().value(),
        price.id().toUuid());
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
