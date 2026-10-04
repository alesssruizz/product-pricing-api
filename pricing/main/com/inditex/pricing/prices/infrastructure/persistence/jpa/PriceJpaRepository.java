package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface PriceJpaRepository extends ListCrudRepository<PriceJpaEntity, Long> {

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
}
