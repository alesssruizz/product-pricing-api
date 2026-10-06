package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.exceptions.InvalidPriceQuantity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PriceQuantity")
class PriceQuantityTest {

  @Test
  void acceptsAPositiveValue() {
    assertThat(new PriceQuantity(new BigDecimal("0.01")).value()).isEqualTo(new BigDecimal("0.01"));
  }

  @Test
  void rejectsZero() {
    assertThatThrownBy(() -> new PriceQuantity(BigDecimal.ZERO))
        .isInstanceOf(InvalidPriceQuantity.class);
  }

  @Test
  void rejectsNegativeValues() {
    var negativeValue = new BigDecimal("-1");
    assertThatThrownBy(() -> new PriceQuantity(negativeValue))
        .isInstanceOf(InvalidPriceQuantity.class);
  }
}
