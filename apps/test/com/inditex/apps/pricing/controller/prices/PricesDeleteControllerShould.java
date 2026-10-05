package com.inditex.apps.pricing.controller.prices;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

  private static final String ENDPOINT = "/api/v1/prices/00000000-0000-0000-0000-000000000001";

  @Nested
  class HappyPathTests {

    @Test
    @DisplayName("Returns 204 with no body and then GET returns 404 for the same id")
    void deletesThePriceAndThenItIsGone() throws Exception {
      perform(delete(ENDPOINT)).andExpect(status().isNoContent()).andExpect(content().string(""));

      perform(get(ENDPOINT))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.errorCode").value("price_not_found"));
    }
  }

  @Nested
  class ErrorPathTests {

    @Test
    @DisplayName("Returns 404 with price_not_found when the id does not exist")
    void returns404WhenPriceDoesNotExist() throws Exception {
      perform(delete("/api/v1/prices/00000000-0000-0000-0000-000000000999"))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.errorCode").value("price_not_found"));
    }

    @Test
    @DisplayName("Returns 400 with invalid_uuid when the id is not a UUID")
    void returns400WhenIdIsMalformed() throws Exception {
      perform(delete("/api/v1/prices/not-a-uuid"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("invalid_uuid"));
    }
  }
}
