package com.inditex.apps.pricing.controller.prices;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
public class PricesPostControllerShould extends ProductPricingApiApplicationTests {

  private static final String ENDPOINT = "/api/v1/prices";

  private static final String VALID_BODY =
      """
      {
          "brandId": 1,
          "productId": 35455,
          "priceList": 9,
          "priority": 5,
          "startDate": "2021-01-01T00:00:00",
          "endDate": "2021-01-31T23:59:59",
          "price": 12.30,
          "currency": "EUR"
      }
      """;

  private ResultActions postBody(String body) throws Exception {
    return perform(post(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  @Nested
  class HappyPathTests {

    @Test
    @DisplayName("Returns 201 with Location and the created price including its id")
    void createsThePrice() throws Exception {
      postBody(VALID_BODY)
          .andExpect(status().isCreated())
          .andExpect(header().string("Location", containsString("/api/v1/prices/")))
          .andExpect(jsonPath("$.id").isNumber())
          .andExpect(jsonPath("$.priceList").value(9))
          .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @DisplayName("Ignores a client supplied id and assigns its own")
    void ignoresClientId() throws Exception {
      String bodyWithId = VALID_BODY.replace("{", "{\n    \"id\": 999,");

      postBody(bodyWithId).andExpect(status().isCreated()).andExpect(jsonPath("$.id", not(999)));
    }
  }

  @Nested
  class ErrorPathTests {

    @Test
    @DisplayName("Returns 409 with price_already_exists when dates and priority collide")
    void returns409OnConflict() throws Exception {
      String conflicting =
          """
          {
              "brandId": 1,
              "productId": 35455,
              "priceList": 9,
              "priority": 0,
              "startDate": "2020-06-14T00:00:00",
              "endDate": "2020-12-31T23:59:59",
              "price": 1.00,
              "currency": "EUR"
          }
          """;

      postBody(conflicting)
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.errorCode").value("price_already_exists"));
    }

    @Test
    @DisplayName("Returns 400 with invalid_reference when the brand does not exist")
    void returns400OnUnknownBrand() throws Exception {
      postBody(VALID_BODY.replace("\"brandId\": 1", "\"brandId\": 999"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("invalid_reference"));
    }

    @Test
    @DisplayName("Returns 400 with invalid_price_quantity when price is zero")
    void returns400OnZeroQuantity() throws Exception {
      postBody(VALID_BODY.replace("\"price\": 12.30", "\"price\": 0"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("invalid_price_quantity"));
    }

    @Test
    @DisplayName("Returns 400 without errorCode when the JSON is malformed")
    void returns400WithoutErrorCodeOnMalformedJson() throws Exception {
      postBody("{\"brandId\": ")
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").doesNotExist());
    }
  }
}
