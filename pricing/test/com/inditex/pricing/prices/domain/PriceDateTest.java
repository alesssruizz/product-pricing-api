package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inditex.pricing.shared.domain.InvalidDateFormat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PriceDate")
class PriceDateTest {

  @Test
  void parsesAnIsoLocalDateTime() {
    new PriceDate("2020-06-14T10:00:00");
  }

  @Test
  void rejectsAMalformedDate() {
    assertThatThrownBy(() -> new PriceDate("not-a-date")).isInstanceOf(InvalidDateFormat.class);
  }
}
