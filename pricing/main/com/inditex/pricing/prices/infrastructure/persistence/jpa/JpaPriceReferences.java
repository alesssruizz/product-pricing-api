package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import com.inditex.pricing.brands.infrastructure.persistence.jpa.BrandJpaRepository;
import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.PriceReferences;
import com.inditex.pricing.prices.domain.ProductId;
import com.inditex.pricing.products.infrastructure.persistence.jpa.ProductJpaRepository;
import com.inditex.pricing.shared.domain.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class JpaPriceReferences implements PriceReferences {

  private final BrandJpaRepository brands;

  private final ProductJpaRepository products;

  public JpaPriceReferences(BrandJpaRepository brands, ProductJpaRepository products) {
    this.brands = brands;
    this.products = products;
  }

  @Override
  public boolean brandExists(BrandId id) {
    return brands.existsById(id.value());
  }

  @Override
  public boolean productExists(ProductId id) {
    return products.existsById(id.value());
  }
}
