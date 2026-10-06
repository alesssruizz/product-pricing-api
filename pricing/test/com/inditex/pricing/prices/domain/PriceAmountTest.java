package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.exceptions.InvalidPriceQuantity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PriceAmount")
class PriceAmountTest {

  @Test
  void acceptsAPositiveValue() {
    assertThat(new PriceAmount(new BigDecimal("0.01")).value()).isEqualTo(new BigDecimal("0.01"));
  }

  @Test
  void rejectsZero() {
    assertThatThrownBy(() -> new PriceAmount(BigDecimal.ZERO))
        .isInstanceOf(InvalidPriceQuantity.class);
  }

  @Test
  void rejectsNegativeValues() {
    var negativeValue = new BigDecimal("-1");
    assertThatThrownBy(() -> new PriceAmount(negativeValue))
        .isInstanceOf(InvalidPriceQuantity.class);
  }
}
