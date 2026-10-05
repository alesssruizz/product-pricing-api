package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.exceptions.InvalidPriceQuantity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PriceQuantity")
class PriceQuantityTest {

  @Test
  void acceptsAPositiveValue() {
    new PriceQuantity(new BigDecimal("0.01"));
  }

  @Test
  void rejectsZero() {
    assertThatThrownBy(() -> new PriceQuantity(BigDecimal.ZERO))
        .isInstanceOf(InvalidPriceQuantity.class);
  }

  @Test
  void rejectsNegativeValues() {
    assertThatThrownBy(() -> new PriceQuantity(new BigDecimal("-1")))
        .isInstanceOf(InvalidPriceQuantity.class);
  }
}
