package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceAmount;
import com.inditex.pricing.prices.domain.PriceCurrency;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceList;
import com.inditex.pricing.prices.domain.PricePriority;
import com.inditex.pricing.prices.domain.ProductId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PriceJpaAdapter")
class PriceJpaAdapterTest {

  private static final String ID = "00000000-0000-0000-0000-000000000001";

  @Mock private PriceJpaRepository repository;

  private PriceJpaAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new PriceJpaAdapter(repository);
  }

  private static Price price() {
    return Price.create(
        new PriceId(ID),
        new BrandId(1L),
        new PriceDate("2020-06-14T15:00:00"),
        new PriceDate("2020-06-14T18:30:00"),
        new PriceList(2),
        new ProductId(35455L),
        new PricePriority(1),
        new PriceAmount(new BigDecimal("25.45")),
        new PriceCurrency("EUR"));
  }

  private static void assertMapsEveryField(PriceJpaEntity entity) {
    assertThat(entity.getId()).isEqualTo(UUID.fromString(ID));
    assertThat(entity.getBrandId()).isEqualTo(1L);
    assertThat(entity.getProductId()).isEqualTo(35455L);
    assertThat(entity.getPriceList()).isEqualTo(2);
    assertThat(entity.getPriority()).isEqualTo(1);
    assertThat(entity.getStartDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 15, 0, 0));
    assertThat(entity.getEndDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 18, 30, 0));
    assertThat(entity.getPrice()).isEqualByComparingTo("25.45");
    assertThat(entity.getCurrency()).isEqualTo("EUR");
  }

  @Nested
  @DisplayName("when creating a price")
  class WhenCreating {

    @Test
    void savesAnEntityFlaggedAsNew() {
      ArgumentCaptor<PriceJpaEntity> saved = ArgumentCaptor.forClass(PriceJpaEntity.class);

      adapter.create(price());

      verify(repository).save(saved.capture());
      PriceJpaEntity entity = saved.getValue();
      assertThat(entity.isNew()).isTrue();
      assertMapsEveryField(entity);
    }
  }

  @Nested
  @DisplayName("when updating a price")
  class WhenUpdating {

    @Test
    void savesAnEntityNotFlaggedAsNew() {
      ArgumentCaptor<PriceJpaEntity> saved = ArgumentCaptor.forClass(PriceJpaEntity.class);

      adapter.update(price());

      verify(repository).save(saved.capture());
      PriceJpaEntity entity = saved.getValue();
      assertThat(entity.isNew()).isFalse();
      assertMapsEveryField(entity);
    }
  }
}
