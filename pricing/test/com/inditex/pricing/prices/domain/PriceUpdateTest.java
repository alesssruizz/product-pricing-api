package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.event.PriceUpdatedDomainEvent;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceDateRange;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Price.update")
class PriceUpdateTest {

  private static final String ID = "00000000-0000-0000-0000-000000000001";

  private static Price update(String id, String startDate, String endDate, String currency) {
    return Price.update(id, 1L, startDate, endDate, 1, 35455L, 0, BigDecimal.TEN, currency);
  }

  private static Price update(String startDate, String endDate, String currency) {
    return update(ID, startDate, endDate, currency);
  }

  @Nested
  @DisplayName("when the parameters are valid")
  class WhenValid {

    @Test
    void registersExactlyOnePriceUpdatedDomainEvent() {
      Price price = update("2020-06-14T00:00:00", "2020-12-31T23:59:59", "EUR");

      assertThat(price.pullDomainEvents())
          .singleElement()
          .isInstanceOfSatisfying(
              PriceUpdatedDomainEvent.class,
              event -> {
                assertThat(event.eventName()).isEqualTo("price.updated");
                assertThat(event.aggregateId()).isEqualTo(ID);
                assertThat(event.brandId()).isEqualTo(1L);
                assertThat(event.productId()).isEqualTo(35455L);
                assertThat(event.priceList()).isEqualTo(1);
                assertThat(event.priority()).isEqualTo(0);
                assertThat(event.startDate()).isEqualTo("2020-06-14T00:00:00");
                assertThat(event.endDate()).isEqualTo("2020-12-31T23:59:59");
                assertThat(event.price()).isEqualByComparingTo(BigDecimal.TEN);
                assertThat(event.currency()).isEqualTo("EUR");
              });
    }
  }

  @Nested
  @DisplayName("when the parameters are invalid")
  class WhenInvalid {

    @Test
    void rejectsAnEndDateNotAfterStartDate() {
      assertThatThrownBy(() -> update("2020-06-14T00:00:00", "2020-06-14T00:00:00", "EUR"))
          .isInstanceOf(InvalidPriceDateRange.class);
    }
  }
}
