package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import com.inditex.pricing.prices.domain.PriceBrandId;
import com.inditex.pricing.prices.domain.PriceProductId;
import com.inditex.pricing.prices.domain.PriceReferences;
import com.inditex.pricing.shared.domain.Service;

import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class JpaPriceReferences implements PriceReferences {

  private final EntityManager entityManager;

  public JpaPriceReferences(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public boolean brandExists(PriceBrandId id) {
    return countById("brands", id.value()) > 0;
  }

  @Override
  public boolean productExists(PriceProductId id) {
    return countById("products", id.value()) > 0;
  }

  private long countById(String table, Long id) {
    Number count =
        (Number)
            entityManager
                .createNativeQuery("SELECT COUNT(*) FROM " + table + " WHERE id = ?1")
                .setParameter(1, id)
                .getSingleResult();
    return count.longValue();
  }
}
