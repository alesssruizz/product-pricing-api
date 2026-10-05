package com.inditex.apps.pricing.controller.prices;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inditex.apps.pricing.ProductPricingApiApplicationTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PricesPostControllerShould extends ProductPricingApiApplicationTests {

  private static final String ENDPOINT = "/api/v1/prices";

  private static final String NEW_ID = "00000000-0000-0000-0000-000000000009";

  private static final String SEED_ID = "00000000-0000-0000-0000-000000000001";

  private static final String VALID_BODY =
      """
      {
          "id": "00000000-0000-0000-0000-000000000009",
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

  @Nested
  class HappyPathTests {

    @Test
    @DisplayName("Returns 201 with Location and the created price with the client supplied id")
    void createsThePrice() throws Exception {
      postBody(ENDPOINT, VALID_BODY)
          .andExpect(status().isCreated())
          .andExpect(header().string("Location", containsString("/api/v1/prices/" + NEW_ID)))
          .andExpect(jsonPath("$.id").value(NEW_ID))
          .andExpect(jsonPath("$.priceList").value(9))
          .andExpect(jsonPath("$.currency").value("EUR"));

      perform(get(ENDPOINT + "/" + NEW_ID))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(NEW_ID))
          .andExpect(jsonPath("$.priceList").value(9));
    }

    @Test
    @DisplayName("Returns 201 when dates match an existing price but priority differs")
    void createsPriceWhenOnlyPriorityDiffers() throws Exception {
      String samePeriodDifferentPriority =
          VALID_BODY
              .replace(
                  "\"startDate\": \"2021-01-01T00:00:00\"",
                  "\"startDate\": \"2020-06-14T00:00:00\"")
              .replace("\"priority\": 5", "\"priority\": 1");

      postBody(ENDPOINT, samePeriodDifferentPriority).andExpect(status().isCreated());
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
              "id": "00000000-0000-0000-0000-000000000009",
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

      postBody(ENDPOINT, conflicting)
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.errorCode").value("price_already_exists"));
    }

    @Test
    @DisplayName("Returns 400 with price_field_required when the id is missing")
    void returns400WhenIdIsMissing() throws Exception {
      postBody(ENDPOINT, VALID_BODY.replace("\"id\": \"" + NEW_ID + "\",", ""))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("price_field_required"));
    }

    @Test
    @DisplayName("Returns 400 with price_field_required when the id is blank")
    void returns400WhenIdIsBlank() throws Exception {
      postBody(ENDPOINT, VALID_BODY.replace(NEW_ID, " "))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("price_field_required"));
    }

    @Test
    @DisplayName("Returns 400 with invalid_uuid when the id is not a UUID")
    void returns400WhenIdIsMalformed() throws Exception {
      postBody(ENDPOINT, VALID_BODY.replace(NEW_ID, "not-a-uuid"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("invalid_uuid"));
    }

    @Test
    @DisplayName(
        "Returns 409 with price_id_already_exists when the id is already taken, "
            + "leaving the existing price unchanged")
    void returns409WhenIdAlreadyExists() throws Exception {
      postBody(ENDPOINT, VALID_BODY.replace(NEW_ID, SEED_ID))
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.errorCode").value("price_id_already_exists"));

      perform(get(ENDPOINT + "/" + SEED_ID))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.priceList").value(1))
          .andExpect(jsonPath("$.price").value(35.50))
          .andExpect(jsonPath("$.startDate").value("2020-06-14T00:00:00"));
    }

    @Test
    @DisplayName(
        "Returns 409 with price_id_already_exists taking precedence over price_already_exists")
    void idConflictTakesPrecedenceOverBusinessKeyConflict() throws Exception {
      String sameIdAndSameKey =
          VALID_BODY
              .replace(NEW_ID, SEED_ID)
              .replace(
                  "\"startDate\": \"2021-01-01T00:00:00\"",
                  "\"startDate\": \"2020-06-14T00:00:00\"")
              .replace(
                  "\"endDate\": \"2021-01-31T23:59:59\"", "\"endDate\": \"2020-12-31T23:59:59\"")
              .replace("\"priority\": 5", "\"priority\": 0");

      postBody(ENDPOINT, sameIdAndSameKey)
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.errorCode").value("price_id_already_exists"));
    }

    @Test
    @DisplayName("Returns 400 with invalid_reference when the brand does not exist")
    void returns400OnUnknownBrand() throws Exception {
      postBody(ENDPOINT, VALID_BODY.replace("\"brandId\": 1", "\"brandId\": 999"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("invalid_reference"));
    }

    @Test
    @DisplayName("Returns 400 with invalid_price_currency when the currency is not ISO 4217")
    void returns400OnInvalidCurrency() throws Exception {
      postBody(ENDPOINT, VALID_BODY.replace("\"currency\": \"EUR\"", "\"currency\": \"ABC\""))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("invalid_price_currency"));
    }

    @Test
    @DisplayName("Returns 400 with price_field_required when the currency is blank")
    void returns400OnBlankCurrency() throws Exception {
      postBody(ENDPOINT, VALID_BODY.replace("\"currency\": \"EUR\"", "\"currency\": \" \""))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("price_field_required"));
    }

    @Test
    @DisplayName("Returns 400 with invalid_price_quantity when price is zero")
    void returns400OnZeroQuantity() throws Exception {
      postBody(ENDPOINT, VALID_BODY.replace("\"price\": 12.30", "\"price\": 0"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").value("invalid_price_quantity"));
    }

    @Test
    @DisplayName("Returns 400 without errorCode when the JSON is malformed")
    void returns400WithoutErrorCodeOnMalformedJson() throws Exception {
      postBody(ENDPOINT, "{\"brandId\": ")
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.errorCode").doesNotExist());
    }
  }
}
