package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.exceptions.InvalidPriceCurrency;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceDateRange;
import com.inditex.pricing.prices.domain.exceptions.PriceFieldRequired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Price.create")
class PriceCreateTest {

  private static Price create(String startDate, String endDate, String currency) {
    return Price.create(1L, 35455L, 1, 0, startDate, endDate, BigDecimal.TEN, currency);
  }

  @Test
  void createsAPriceWithoutIdWhenAllFieldsAreValid() {
    Price price = create("2020-06-14T00:00:00", "2020-12-31T23:59:59", "EUR");

    assertThat(price.id()).isNull();
  }

  @Test
  void rejectsAMissingField() {
    assertThatThrownBy(
            () ->
                Price.create(
                    1L,
                    35455L,
                    1,
                    0,
                    "2020-06-14T00:00:00",
                    "2020-12-31T23:59:59",
                    BigDecimal.TEN,
                    null))
        .isInstanceOf(PriceFieldRequired.class);
  }

  @Test
  void rejectsAnEndDateNotAfterStartDate() {
    assertThatThrownBy(() -> create("2020-06-14T00:00:00", "2020-06-14T00:00:00", "EUR"))
        .isInstanceOf(InvalidPriceDateRange.class);
  }

  @Test
  void rejectsAnInvalidCurrency() {
    assertThatThrownBy(() -> create("2020-06-14T00:00:00", "2020-12-31T23:59:59", "ABC"))
        .isInstanceOf(InvalidPriceCurrency.class);
  }
}
