package com.inditex.pricing.prices.application.findbyid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inditex.pricing.prices.application.PriceResponse;
import com.inditex.pricing.prices.domain.PriceId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("FindPriceByIdQueryHandler")
class FindPriceByIdQueryHandlerTest {

  private static final String ID = "00000000-0000-0000-0000-000000000042";

  @Mock private PriceByIdFinder finder;

  @Mock private PriceResponse response;

  private FindPriceByIdQueryHandler handler;

  @BeforeEach
  void setUp() {
    handler = new FindPriceByIdQueryHandler(finder);
  }

  @Nested
  @DisplayName("Mapping the query id")
  class MappingScenarios {

    @Test
    @DisplayName("Builds a PriceId from the query id and returns the finder result")
    void mapsQueryIdToPriceId() {
      when(finder.find(new PriceId(ID))).thenReturn(response);

      var result = handler.handle(new FindPriceByIdQuery(ID));

      assertThat(result).isSameAs(response);
      verify(finder).find(new PriceId(ID));
    }
  }
}
