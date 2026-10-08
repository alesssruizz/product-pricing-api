package com.inditex.pricing.products.infrastructure.persistence.jpa;

import org.springframework.data.repository.Repository;

public interface ProductJpaRepository extends Repository<ProductJpaEntity, Long> {

  boolean existsById(Long id);

  ProductJpaEntity getReferenceById(Long id);
}
