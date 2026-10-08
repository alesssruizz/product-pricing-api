package com.inditex.pricing.brands.domain;

import com.inditex.pricing.shared.domain.AggregateRoot;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Getter
public final class Brand extends AggregateRoot {

  @EqualsAndHashCode.Include private final BrandId id;

  private final BrandName name;

  public Brand(Long id, String name) {
    this.id = new BrandId(id);
    this.name = new BrandName(name);
  }
}
