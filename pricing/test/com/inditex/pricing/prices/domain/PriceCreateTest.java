package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.exceptions.InvalidPriceCurrency;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceDateRange;
import com.inditex.pricing.shared.domain.exception.FieldRequired;
import com.inditex.pricing.shared.domain.exception.InvalidUUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Price.create")
class PriceCreateTest {

  private static final String ID = "00000000-0000-0000-0000-000000000001";

  private static Price create(String id, String startDate, String endDate, String currency) {
    return Price.create(id, 1L, 35455L, 1, 0, startDate, endDate, BigDecimal.TEN, currency);
  }

  private static Price create(String startDate, String endDate, String currency) {
    return create(ID, startDate, endDate, currency);
  }

  @Test
  void createsAPriceWithTheGivenIdWhenAllFieldsAreValid() {
    Price price = create("2020-06-14T00:00:00", "2020-12-31T23:59:59", "EUR");

    assertThat(price.id().value()).isEqualTo(ID);
  }

  @Test
  void rejectsANullId() {
    assertThatThrownBy(() -> create(null, "2020-06-14T00:00:00", "2020-12-31T23:59:59", "EUR"))
        .isInstanceOf(FieldRequired.class);
  }

  @Test
  void rejectsABlankId() {
    assertThatThrownBy(() -> create("  ", "2020-06-14T00:00:00", "2020-12-31T23:59:59", "EUR"))
        .isInstanceOf(FieldRequired.class);
  }

  @Test
  void rejectsAMalformedId() {
    assertThatThrownBy(
            () -> create("not-a-uuid", "2020-06-14T00:00:00", "2020-12-31T23:59:59", "EUR"))
        .isInstanceOf(InvalidUUID.class);
  }

  @Test
  void rejectsABlankCurrency() {
    assertThatThrownBy(() -> create("2020-06-14T00:00:00", "2020-12-31T23:59:59", " "))
        .isInstanceOf(FieldRequired.class);
  }

  @Test
  void rejectsAMissingField() {
    assertThatThrownBy(
            () ->
                Price.create(
                    ID,
                    1L,
                    35455L,
                    1,
                    0,
                    "2020-06-14T00:00:00",
                    "2020-12-31T23:59:59",
                    BigDecimal.TEN,
                    null))
        .isInstanceOf(FieldRequired.class);
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
