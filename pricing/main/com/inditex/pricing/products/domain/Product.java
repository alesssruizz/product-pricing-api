package com.inditex.pricing.products.domain;

import com.inditex.pricing.shared.domain.AggregateRoot;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
public final class Product extends AggregateRoot {

  @EqualsAndHashCode.Include private final ProductId id;

  private final ProductName name;

  public Product(Long id, String name) {
    this.id = new ProductId(id);
    this.name = new ProductName(name);
  }
}
