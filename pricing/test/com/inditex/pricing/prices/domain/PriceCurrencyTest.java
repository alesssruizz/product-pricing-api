package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inditex.pricing.prices.domain.exception.InvalidPriceCurrency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PriceCurrency")
class PriceCurrencyTest {

  @Test
  void acceptsAnIso4217Code() {
    assertThat(new PriceCurrency("EUR").value()).isEqualTo("EUR");
  }

  @Test
  void rejectsAnUnknownCode() {
    assertThatThrownBy(() -> new PriceCurrency("ABC")).isInstanceOf(InvalidPriceCurrency.class);
  }

  @Test
  void rejectsLowercaseCode() {
    assertThatThrownBy(() -> new PriceCurrency("eur")).isInstanceOf(InvalidPriceCurrency.class);
  }
}
