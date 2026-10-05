package com.inditex.apps.pricing.controller.prices;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inditex.apps.pricing.ProductPricingApiApplicationTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PricesDeleteControllerShould extends ProductPricingApiApplicationTests {

  private static final String ENDPOINT = "/api/v1/prices/1";

  @Nested
  class HappyPathTests {

    @Test
    @DisplayName("Returns 204 with no body and then 404 for the same id")
    void deletesThePriceAndThenItIsGone() throws Exception {
      perform(delete(ENDPOINT)).andExpect(status().isNoContent()).andExpect(content().string(""));

      perform(delete(ENDPOINT))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.errorCode").value("price_not_found"));
    }
  }

  @Nested
  class ErrorPathTests {

    @Test
    @DisplayName("Returns 404 with price_not_found when the id does not exist")
    void returns404WhenPriceDoesNotExist() throws Exception {
      perform(delete("/api/v1/prices/999"))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.errorCode").value("price_not_found"));
    }
  }
}
