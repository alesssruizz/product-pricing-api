package com.inditex.pricing.prices.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceAmount;
import com.inditex.pricing.prices.domain.PriceCurrency;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceList;
import com.inditex.pricing.prices.domain.PricePriority;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.ProductId;
import com.inditex.pricing.prices.domain.exception.PriceAlreadyExists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PriceConflictPolicy")
class PriceConflictPolicyTest {

  @Mock private PriceRepository repository;

  private PriceConflictPolicy policy;

  @BeforeEach
  void setUp() {
    policy = new PriceConflictPolicy(repository);
  }

  private static Price price() {
    return Price.create(
        new PriceId("00000000-0000-0000-0000-000000000001"),
        new BrandId(1L),
        new PriceDate("2020-06-14T00:00:00"),
        new PriceDate("2020-12-31T23:59:59"),
        new PriceList(1),
        new ProductId(35455L),
        new PricePriority(0),
        new PriceAmount(BigDecimal.TEN),
        new PriceCurrency("EUR"));
  }

  @Nested
  @DisplayName("when a conflicting price exists")
  class WhenConflict {

    @Test
    void rejectsWithPriceAlreadyExists() {
      when(repository.existsConflict(any())).thenReturn(true);

      assertThatThrownBy(() -> policy.ensureNoConflict(price()))
          .isInstanceOfSatisfying(
              PriceAlreadyExists.class,
              error -> assertThat(error.errorCode()).isEqualTo("price_already_exists"));
    }
  }

  @Nested
  @DisplayName("when there is no conflict")
  class WhenNoConflict {

    @Test
    void allowsThePrice() {
      when(repository.existsConflict(any())).thenReturn(false);

      assertThatCode(() -> policy.ensureNoConflict(price())).doesNotThrowAnyException();
    }
  }
}
