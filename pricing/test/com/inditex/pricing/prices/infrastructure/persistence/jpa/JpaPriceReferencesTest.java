package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.inditex.pricing.brands.infrastructure.persistence.jpa.BrandJpaRepository;
import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.ProductId;
import com.inditex.pricing.products.infrastructure.persistence.jpa.ProductJpaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("JpaPriceReferences")
class JpaPriceReferencesTest {

  @Mock private BrandJpaRepository brands;

  @Mock private ProductJpaRepository products;

  private JpaPriceReferences references;

  @BeforeEach
  void setUp() {
    references = new JpaPriceReferences(brands, products);
  }

  @Nested
  @DisplayName("when checking a brand")
  class WhenCheckingBrand {

    @Test
    void returnsTrueWhenTheBrandExists() {
      when(brands.existsById(1L)).thenReturn(true);

      assertThat(references.brandExists(new BrandId(1L))).isTrue();
    }

    @Test
    void returnsFalseWhenTheBrandDoesNotExist() {
      when(brands.existsById(99L)).thenReturn(false);

      assertThat(references.brandExists(new BrandId(99L))).isFalse();
    }
  }

  @Nested
  @DisplayName("when checking a product")
  class WhenCheckingProduct {

    @Test
    void returnsTrueWhenTheProductExists() {
      when(products.existsById(35455L)).thenReturn(true);

      assertThat(references.productExists(new ProductId(35455L))).isTrue();
    }

    @Test
    void returnsFalseWhenTheProductDoesNotExist() {
      when(products.existsById(99L)).thenReturn(false);

      assertThat(references.productExists(new ProductId(99L))).isFalse();
    }
  }
}
