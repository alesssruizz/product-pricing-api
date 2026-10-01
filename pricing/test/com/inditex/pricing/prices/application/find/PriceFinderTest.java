package com.inditex.pricing.prices.application.find;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import com.inditex.pricing.prices.application.PriceResponse;
import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceBrandId;
import com.inditex.pricing.prices.domain.PriceCurrency;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceList;
import com.inditex.pricing.prices.domain.PriceNotFoundException;
import com.inditex.pricing.prices.domain.PricePriority;
import com.inditex.pricing.prices.domain.PriceProductId;
import com.inditex.pricing.prices.domain.PriceQuantity;
import com.inditex.pricing.prices.domain.PriceRepository;

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
    return new Price(
        new PriceBrandId(1L),
        new PriceDate("2020-06-14T00:00:00"),
        new PriceDate("2020-12-31T23:59:59"),
        new PriceList(1),
        new PriceProductId(35455L),
        new PricePriority(0),
        new PriceQuantity(BigDecimal.TEN),
        new PriceCurrency("EUR"));
  }

  @Nested
  @DisplayName("Test 1: example")
  class WhenPriceExists {

    @Test
    void returnsThePriceFromTheRepository() {
      Price price = buildPrice();
      when(repository.findApplicablePrice(any(), any(), any())).thenReturn(Optional.of(price));

      PriceResponse response =
          finder.find(new FindApplicablePriceQuery(1L, 35455L, "2020-06-14T10:00:00"));

      assertThat(response).isEqualTo(PriceResponse.fromAggregate(price));
    }
  }

  @Nested
  @DisplayName("Test 2: when no price matches")
  class WhenPriceDoesNotExist {

    @Test
    void throwsPriceNotFoundException() {
      when(repository.findApplicablePrice(any(), any(), any())).thenReturn(Optional.empty());

      assertThatThrownBy(
              () -> finder.find(new FindApplicablePriceQuery(1L, 35455L, "2020-06-14T10:00:00")))
          .isInstanceOf(PriceNotFoundException.class);
    }
  }
}
