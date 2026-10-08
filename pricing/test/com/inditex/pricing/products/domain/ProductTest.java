package com.inditex.pricing.products.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Product")
class ProductTest {

  @Test
  void exposesIdAndName() {
    var product = new Product(1L, "Sample");

    assertThat(product.id()).isEqualTo(new ProductId(1L));
    assertThat(product.name()).isEqualTo(new ProductName("Sample"));
  }

  @Test
  void isEqualWhenIdMatchesRegardlessOfName() {
    assertThat(new Product(1L, "Sample"))
        .isEqualTo(new Product(1L, "Renamed"))
        .hasSameHashCodeAs(new Product(1L, "Renamed"));
    assertThat(new Product(1L, "Sample")).isNotEqualTo(new Product(2L, "Sample"));
  }
}
