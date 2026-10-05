package com.inditex.apps.pricing.controller.prices;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inditex.apps.pricing.ProductPricingApiApplicationTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PricesPutControllerShould extends ProductPricingApiApplicationTests {

  private static final String ENDPOINT = "/api/v1/prices/1";

  private static final String VALID_BODY =
      """
      {
          "brandId": 1,
          "productId": 35455,
          "priceList": 9,
          "priority": 0,
          "startDate": "2020-06-14T00:00:00",
          "endDate": "2020-12-31T23:59:59",
          "price": 40.00,
          "currency": "EUR"
      }
      """;

  private ResultActions putBody(String endpoint, String body) throws Exception {
    return perform(put(endpoint).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  @Nested
  class HappyPathTests {

    @Test
    @DisplayName("Returns 200 with the replaced price keeping the path id")
    void replacesThePrice() throws Exception {
      putBody(ENDPOINT, VALID_BODY.replace("\"priceList\": 9", "\"priceList\": 9, \"id\": 999"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.priceList").value(9))
          .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @DisplayName("Returns 200 when the price keeps its own dates and priority key")
    void allowsItsOwnKey() throws Exception {
      putBody(ENDPOINT, VALID_BODY.replace("\"price\": 40.00", "\"price\": 41.00"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1));
    }
  }

  @Nested
  class ErrorPathTests {

    @Test
    @DisplayName("Returns 404 with price_not_found when the id does not exist")
    void returns404WhenPriceDoesNotExist() throws Exception {
      putBody("/api/v1/prices/999", VALID_BODY)
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.errorCode").value("price_not_found"));
    }

    @Test
    @DisplayName("Returns 409 with price_already_exists when it collides with another price")
    void returns409OnConflictWithAnotherPrice() throws Exception {
      putBody("/api/v1/prices/2", VALID_BODY.replace("\"priceList\": 9", "\"priceList\": 2"))
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.errorCode").value("price_already_exists"));
    }

    @Test
    @DisplayName("Returns 400 with invalid_reference when the brand does not exist")
    void returns400OnUnknownBrand() throws Exception {
      putBody(ENDPOINT, VALID_BODY.replace("\"brandId\": 1", "\"brandId\": 999"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("invalid_reference"));
    }

    @Test
    @DisplayName("Returns 400 with price_field_required when priceList is missing")
    void returns400WhenPriceListIsMissing() throws Exception {
      putBody(ENDPOINT, VALID_BODY.replace("\"priceList\": 9,", ""))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("price_field_required"));
    }

    @Test
    @DisplayName("Returns 400 without errorCode when the JSON is malformed")
    void returns400WithoutErrorCodeOnMalformedJson() throws Exception {
      putBody(ENDPOINT, "{\"brandId\": ")
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").doesNotExist());
    }
  }
}
