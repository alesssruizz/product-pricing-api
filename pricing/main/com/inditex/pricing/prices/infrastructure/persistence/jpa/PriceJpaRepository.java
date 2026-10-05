package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface PriceJpaRepository extends ListCrudRepository<PriceJpaEntity, UUID> {

  @Query(
      """
      SELECT p
      FROM PriceJpaEntity p
      WHERE p.brandId = :brandId
          AND p.productId = :productId
          AND :applicationDate BETWEEN p.startDate AND p.endDate
      ORDER BY p.priority DESC, p.startDate DESC
      """)
  List<PriceJpaEntity> findApplicablePrice(
      Long brandId, Long productId, LocalDateTime applicationDate, Limit limit);

  @Query(
      """
      SELECT COUNT(p)
      FROM PriceJpaEntity p
      WHERE p.brandId = :brandId
          AND p.productId = :productId
          AND p.priority = :priority
          AND p.startDate = :startDate
          AND p.id <> :id
      """)
  long countConflicts(
      Long brandId, Long productId, Integer priority, LocalDateTime startDate, UUID id);
}
