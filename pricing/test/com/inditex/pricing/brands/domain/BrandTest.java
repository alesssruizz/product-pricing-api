package com.inditex.pricing.brands.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Brand")
class BrandTest {

  @Test
  void exposesIdAndName() {
    var brand = new Brand(1L, "Sample");

    assertThat(brand.id()).isEqualTo(new BrandId(1L));
    assertThat(brand.name()).isEqualTo(new BrandName("Sample"));
  }

  @Test
  void isEqualWhenIdMatchesRegardlessOfName() {
    assertThat(new Brand(1L, "Sample"))
        .isEqualTo(new Brand(1L, "Renamed"))
        .hasSameHashCodeAs(new Brand(1L, "Renamed"));
    assertThat(new Brand(1L, "Sample")).isNotEqualTo(new Brand(2L, "Sample"));
  }
}
