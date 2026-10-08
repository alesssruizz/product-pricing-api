package com.inditex.pricing.brands.infrastructure.persistence.jpa;

import org.springframework.data.repository.Repository;

public interface BrandJpaRepository extends Repository<BrandJpaEntity, Long> {

  boolean existsById(Long id);

  BrandJpaEntity getReferenceById(Long id);
}
