package com.inditex.apps.pricing;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceAmount;
import com.inditex.pricing.prices.domain.PriceCurrency;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceList;
import com.inditex.pricing.prices.domain.PricePriority;
import com.inditex.pricing.prices.domain.ProductId;
import com.inditex.pricing.prices.infrastructure.persistence.jpa.PriceJpaAdapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PriceConflictQueryShould extends ProductPricingApiApplicationTests {

  private static final String SEED_ID = "00000000-0000-0000-0000-000000000001";

  private static final String NEW_ID = "00000000-0000-0000-0000-000000000009";

  private static final long SEED_BRAND_ID = 1L;

  private static final long SEED_PRODUCT_ID = 35455L;

  private static final int SEED_PRIORITY = 0;

  private static final String SEED_START_DATE = "2020-06-14T00:00:00";

  @Autowired private PriceJpaAdapter adapter;

  @Test
  @DisplayName("detect a conflict for a new price matching the seed row")
  void detect_conflict_for_new_price_matching_seed() {
    Price candidate = candidate(NEW_ID, SEED_PRIORITY);

    assertThat(adapter.existsConflict(candidate)).isTrue();
  }

  @Test
  @DisplayName("ignore the seed row itself when checking its own conflict")
  void not_conflict_with_itself() {
    Price candidate = candidate(SEED_ID, SEED_PRIORITY);

    assertThat(adapter.existsConflict(candidate)).isFalse();
  }

  @Test
  @DisplayName("not detect a conflict when the priority differs")
  void not_conflict_when_priority_differs() {
    Price candidate = candidate(NEW_ID, SEED_PRIORITY + 1);

    assertThat(adapter.existsConflict(candidate)).isFalse();
  }

  private Price candidate(String id, int priority) {
    return Price.create(
        new PriceId(id),
        new BrandId(SEED_BRAND_ID),
        new PriceDate(SEED_START_DATE),
        new PriceDate("2020-12-31T23:59:59"),
        new PriceList(1),
        new ProductId(SEED_PRODUCT_ID),
        new PricePriority(priority),
        new PriceAmount(new BigDecimal("35.50")),
        new PriceCurrency("EUR"));
  }
}
