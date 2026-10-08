package com.inditex.pricing.prices.application.find;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import com.inditex.pricing.prices.application.ApplicablePriceResponse;
import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.ProductId;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PriceFinder")
class PriceFinderTest {

  @Mock private PriceRepository repository;

  private PriceFinder finder;

  @BeforeEach
  void setUp() {
    finder = new PriceFinder(repository);
  }

  private static Price buildPrice() {
    return Price.create(
        "00000000-0000-0000-0000-000000000001",
        1L,
        "2020-06-14T00:00:00",
        "2020-12-31T23:59:59",
        1,
        35455L,
        0,
        BigDecimal.TEN,
        "EUR");
  }

  @Nested
  @DisplayName("Test 1: example")
  class WhenPriceExists {

    @Test
    void returnsThePriceFromTheRepository() {
      Price price = buildPrice();
      when(repository.findApplicablePrice(any(), any(), any())).thenReturn(Optional.of(price));

      ApplicablePriceResponse response =
          finder.find(new BrandId(1L), new ProductId(35455L), new PriceDate("2020-06-14T10:00:00"));

      assertThat(response).isEqualTo(ApplicablePriceResponse.fromAggregate(price));
    }
  }

  @Nested
  @DisplayName("Test 2: when no price matches")
  class WhenPriceDoesNotExist {

    @Test
    void throwsPriceNotFoundException() {
      when(repository.findApplicablePrice(any(), any(), any())).thenReturn(Optional.empty());

      assertThatThrownBy(
              () ->
                  finder.find(
                      new BrandId(1L), new ProductId(35455L), new PriceDate("2020-06-14T10:00:00")))
          .isInstanceOf(PriceNotFoundException.class);
    }
  }
}
