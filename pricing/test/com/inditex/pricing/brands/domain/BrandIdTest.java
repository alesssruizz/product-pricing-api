package com.inditex.pricing.brands.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inditex.pricing.shared.domain.exception.FieldRequired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BrandId")
class BrandIdTest {

  @Test
  void acceptsAValue() {
    assertThat(new BrandId(1L).value()).isEqualTo(1L);
  }

  @Test
  void rejectsNull() {
    assertThatThrownBy(() -> new BrandId(null))
        .isInstanceOfSatisfying(
            FieldRequired.class,
            error -> assertThat(error.errorCode()).isEqualTo("field_required"));
  }
}
