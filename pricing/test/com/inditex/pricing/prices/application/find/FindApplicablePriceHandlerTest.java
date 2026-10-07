package com.inditex.pricing.prices.application.find;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inditex.pricing.prices.application.ApplicablePriceResponse;

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
    @DisplayName("Delegates the query to PriceFinder and returns its response")
    void delegatesToFinder() {
      var query = new FindApplicablePriceQuery(1L, 35455L, "2020-06-14T10:00:00");
      when(finder.find(query)).thenReturn(response);

      var result = handler.handle(query);

      assertThat(result).isSameAs(response);
      verify(finder).find(query);
    }
  }
}
