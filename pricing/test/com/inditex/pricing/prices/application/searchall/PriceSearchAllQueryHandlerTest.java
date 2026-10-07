package com.inditex.pricing.prices.application.searchall;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inditex.pricing.prices.application.PricesResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PriceSearchAllQueryHandler")
class PriceSearchAllQueryHandlerTest {

  @Mock private PriceSearcher searcher;

  @Mock private PricesResponse response;

  private PriceSearchAllQueryHandler handler;

  @BeforeEach
  void setUp() {
    handler = new PriceSearchAllQueryHandler(searcher);
  }

  @Nested
  @DisplayName("Delegation to PriceSearcher")
  class DelegationScenarios {

    @Test
    @DisplayName("Returns the result of PriceSearcher.search ignoring the empty query")
    void delegatesToSearcher() {
      when(searcher.search()).thenReturn(response);

      var result = handler.handle(new PriceSearchAllQuery());

      assertThat(result).isSameAs(response);
      verify(searcher).search();
    }
  }
}
