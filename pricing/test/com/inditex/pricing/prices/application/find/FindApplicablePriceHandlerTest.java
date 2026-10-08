package com.inditex.pricing.prices.application.find;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inditex.pricing.prices.application.ApplicablePriceResponse;
import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.ProductId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindApplicablePriceHandler")
class FindApplicablePriceHandlerTest {

  @Mock private PriceFinder finder;

  @Mock private ApplicablePriceResponse response;

  private FindApplicablePriceHandler handler;

  @BeforeEach
  void setUp() {
    handler = new FindApplicablePriceHandler(finder);
  }

  @Nested
  @DisplayName("Delegation to PriceFinder")
  class DelegationScenarios {

    @Test
    @DisplayName("Converts the query to value objects and delegates to PriceFinder")
    void delegatesToFinder() {
      var query = new FindApplicablePriceQuery(1L, 35455L, "2020-06-14T10:00:00");
      when(finder.find(
              new BrandId(1L), new ProductId(35455L), new PriceDate("2020-06-14T10:00:00")))
          .thenReturn(response);

      var result = handler.handle(query);

      assertThat(result).isSameAs(response);
      verify(finder)
          .find(new BrandId(1L), new ProductId(35455L), new PriceDate("2020-06-14T10:00:00"));
    }
  }
}
