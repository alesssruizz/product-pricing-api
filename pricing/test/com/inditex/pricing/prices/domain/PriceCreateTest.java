package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.event.PriceCreatedDomainEvent;
import com.inditex.pricing.prices.domain.exception.InvalidPriceDateRange;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Price.create")
class PriceCreateTest {

  private static final String ID = "00000000-0000-0000-0000-000000000001";

  private static final String OTHER_ID = "00000000-0000-0000-0000-000000000002";

  private static Price create(String startDate, String endDate) {
    return create(ID, startDate, endDate);
  }

  private static Price create(String id, String startDate, String endDate) {
    return Price.create(
        new PriceId(id),
        new BrandId(1L),
        new PriceDate(startDate),
        new PriceDate(endDate),
        new PriceList(1),
        new ProductId(35455L),
        new PricePriority(0),
        new PriceAmount(BigDecimal.TEN),
        new PriceCurrency("EUR"));
  }

  @Test
  void createsAPriceWithTheGivenIdWhenAllFieldsAreValid() {
    Price price = create("2020-06-14T00:00:00", "2020-12-31T23:59:59");

    assertThat(price.id().value()).isEqualTo(ID);
  }

  @Test
  void registersExactlyOnePriceCreatedDomainEvent() {
    Price price = create("2020-06-14T00:00:00", "2020-12-31T23:59:59");

    assertThat(price.pullDomainEvents())
        .singleElement()
        .isInstanceOfSatisfying(
            PriceCreatedDomainEvent.class,
            event -> {
              assertThat(event.eventName()).isEqualTo("price.created");
              assertThat(event.aggregateId()).isEqualTo(ID);
            });
  }

  @Test
  void isEqualWhenIdMatchesRegardlessOfOtherFields() {
    Price price = create("2020-06-14T00:00:00", "2020-12-31T23:59:59");
    Price samePriceWithOtherDates = create("2021-01-01T00:00:00", "2021-12-31T23:59:59");

    assertThat(price).isEqualTo(samePriceWithOtherDates).hasSameHashCodeAs(samePriceWithOtherDates);
    assertThat(price).isNotEqualTo(create(OTHER_ID, "2020-06-14T00:00:00", "2020-12-31T23:59:59"));
  }

  @Test
  void rejectsAnEndDateNotAfterStartDate() {
    assertThatThrownBy(() -> create("2020-06-14T00:00:00", "2020-06-14T00:00:00"))
        .isInstanceOf(InvalidPriceDateRange.class);
  }
}
