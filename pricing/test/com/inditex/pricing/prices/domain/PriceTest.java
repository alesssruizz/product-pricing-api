package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Price")
class PriceTest {

  private static Price priceWith(int priority, String startDate) {
    return new Price(
        new PriceBrandId(1L),
        new PriceDate(startDate),
        new PriceDate("2020-12-31T23:59:59"),
        new PriceList(1),
        new PriceProductId(35455L),
        new PricePriority(priority),
        new PriceQuantity(BigDecimal.TEN),
        new PriceCurrency("EUR"));
  }

  @Nested
  @DisplayName("mostApplicable")
  class MostApplicable {

    @Test
    @DisplayName("returns empty when there are no candidates")
    void returnsEmptyWhenNoCandidates() {
      Optional<Price> result = Price.mostApplicable(List.of());

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("picks the candidate with the highest priority when priorities differ")
    void picksHighestPriorityWhenPrioritiesDiffer() {
      Price lowerPriority = priceWith(0, "2020-06-14T00:00:00");
      Price higherPriority = priceWith(1, "2020-06-14T15:00:00");

      Optional<Price> result = Price.mostApplicable(List.of(lowerPriority, higherPriority));

      assertThat(result).contains(higherPriority);
    }

    @Test
    @DisplayName("breaks ties by the latest startDate when priorities are equal")
    void breaksTiesByLatestStartDateWhenPrioritiesAreEqual() {
      Price earlierStart = priceWith(1, "2020-06-14T00:00:00");
      Price laterStart = priceWith(1, "2020-06-15T00:00:00");

      Optional<Price> result = Price.mostApplicable(List.of(earlierStart, laterStart));

      assertThat(result).contains(laterStart);
    }
  }
}
