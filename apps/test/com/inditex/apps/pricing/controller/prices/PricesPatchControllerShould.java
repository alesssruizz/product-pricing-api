package com.inditex.apps.pricing.controller.prices;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
public class PricesPatchControllerShould extends ProductPricingApiApplicationTests {

  private static final String ENDPOINT = "/api/v1/prices/00000000-0000-0000-0000-000000000001";

  private ResultActions patchBody(String endpoint, String body) throws Exception {
    return perform(patch(endpoint).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  @Nested
  class HappyPathTests {

    @Test
    @DisplayName("Returns 200 with the price keeping the fields that were not sent")
    void mergesOnlyTheSentFields() throws Exception {
      patchBody("/api/v1/prices/00000000-0000-0000-0000-000000000002", "{\"priority\": 0}")
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value("00000000-0000-0000-0000-000000000002"))
          .andExpect(jsonPath("$.priority").value(0))
          .andExpect(jsonPath("$.startDate").value("2020-06-14T15:00:00"))
          .andExpect(jsonPath("$.priceList").value(2));
    }

    @Test
    @DisplayName("Returns 200 when only the price changes and the own key is kept")
    void allowsItsOwnKey() throws Exception {
      patchBody(ENDPOINT, "{\"price\": 41.00}")
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value("00000000-0000-0000-0000-000000000001"))
          .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    @DisplayName("Ignores an id sent in the body and keeps the path id")
    void ignoresBodyId() throws Exception {
      patchBody(
              "/api/v1/prices/00000000-0000-0000-0000-000000000002",
              "{\"id\": \"00000000-0000-0000-0000-000000000999\", \"price\": 30.00}")
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value("00000000-0000-0000-0000-000000000002"));
    }
  }

  @Nested
  class ErrorPathTests {

    @Test
    @DisplayName("Returns 404 with price_not_found when the id does not exist")
    void returns404WhenPriceDoesNotExist() throws Exception {
      patchBody("/api/v1/prices/00000000-0000-0000-0000-000000000999", "{\"price\": 40.00}")
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.errorCode").value("price_not_found"));
    }

    @Test
    @DisplayName(
        "Returns 400 with invalid_price_date_range when the merged endDate precedes startDate")
    void validatesTheMergedFinalState() throws Exception {
      patchBody(ENDPOINT, "{\"endDate\": \"2020-06-13T00:00:00\"}")
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("invalid_price_date_range"));
    }

    @Test
    @DisplayName(
        "Returns 409 with price_already_exists when the merged key collides with another price")
    void returns409OnCollisionWithAnotherPrice() throws Exception {
      patchBody(
              "/api/v1/prices/00000000-0000-0000-0000-000000000002",
              "{\"priority\": 0, \"startDate\": \"2020-06-14T00:00:00\"}")
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.errorCode").value("price_already_exists"));
    }

    @Test
    @DisplayName("Returns 400 with invalid_uuid when the path id is not a UUID")
    void returns400WhenPathIdIsMalformed() throws Exception {
      patchBody("/api/v1/prices/not-a-uuid", "{\"price\": 40.00}")
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("invalid_uuid"));
    }

    @Test
    @DisplayName("Returns 400 with invalid_reference when the brand does not exist")
    void returns400OnUnknownBrand() throws Exception {
      patchBody(ENDPOINT, "{\"brandId\": 999}")
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("invalid_reference"));
    }

    @Test
    @DisplayName("Returns 400 with invalid_price_quantity when the merged price is zero")
    void returns400OnZeroQuantity() throws Exception {
      patchBody(ENDPOINT, "{\"price\": 0}")
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("invalid_price_quantity"));
    }

    @Test
    @DisplayName("Returns 400 with invalid_price_currency when the currency is not ISO 4217")
    void returns400OnInvalidCurrency() throws Exception {
      patchBody(ENDPOINT, "{\"currency\": \"ABC\"}")
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("invalid_price_currency"));
    }

    @Test
    @DisplayName("Returns 400 without errorCode when the JSON is malformed")
    void returns400WithoutErrorCodeOnMalformedJson() throws Exception {
      patchBody(ENDPOINT, "{\"price\": ")
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").doesNotExist());
    }
  }
}
