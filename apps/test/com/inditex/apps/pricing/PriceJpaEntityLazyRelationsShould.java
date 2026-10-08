package com.inditex.apps.pricing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import com.inditex.pricing.prices.infrastructure.persistence.jpa.PriceJpaEntity;
import com.inditex.pricing.prices.infrastructure.persistence.jpa.PriceJpaRepository;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PriceJpaEntityLazyRelationsShould extends ProductPricingApiApplicationTests {

  private static final UUID SEED_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

  private static final long SEED_BRAND_ID = 1L;

  private static final long SEED_PRODUCT_ID = 35455L;

  @Autowired private PriceJpaRepository repository;

  @Autowired private EntityManagerFactory entityManagerFactory;

  @Test
  @DisplayName("expose brand and product ids from detached lazy proxies without initializing them")
  void expose_ids_without_initializing_proxies() {
    PriceJpaEntity entity = repository.findById(SEED_ID).orElseThrow();

    assertThat(entity.getBrandId()).isEqualTo(SEED_BRAND_ID);
    assertThat(entity.getProductId()).isEqualTo(SEED_PRODUCT_ID);
    var unitUtil = entityManagerFactory.getPersistenceUnitUtil();
    assertThat(unitUtil.isLoaded(entity, "brand")).isFalse();
    assertThat(unitUtil.isLoaded(entity, "product")).isFalse();
  }
}
