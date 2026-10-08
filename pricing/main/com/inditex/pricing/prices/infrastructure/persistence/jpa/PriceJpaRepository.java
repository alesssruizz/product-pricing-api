package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface PriceJpaRepository extends ListCrudRepository<PriceJpaEntity, UUID> {

  @Query(
      """
      SELECT p
      FROM PriceJpaEntity p
      WHERE p.brand.id = :brandId
          AND p.product.id = :productId
          AND :applicationDate BETWEEN p.startDate AND p.endDate
      ORDER BY p.priority DESC, p.startDate DESC
      LIMIT 1
      """)
  List<PriceJpaEntity> findApplicablePrice(
      Long brandId, Long productId, LocalDateTime applicationDate);

  boolean existsByBrand_IdAndProduct_IdAndPriorityAndStartDateAndIdNot(
      Long brandId, Long productId, Integer priority, LocalDateTime startDate, UUID id);
}
