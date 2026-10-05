package com.inditex.apps.pricing.controller.prices;

import java.util.Locale;
import java.util.stream.Stream;

import com.inditex.apps.pricing.ProductPricingApiApplicationTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PricesGetApplicableControllerShould extends ProductPricingApiApplicationTests {

  private static final String ENDPOINT =
      "/api/v1/prices/find?applicationDate=%s&productId=35455&brandId=1";

  static Stream<Arguments> happyPathScenarios() {
    return Stream.of(
        Arguments.of(
            "Test 1: petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)",
            "2020-06-14T10:00:00",
            1,
            "2020-06-14T00:00:00",
            "2020-12-31T23:59:59",
            35.50),
        Arguments.of(
            "Test 2: petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)",
            "2020-06-14T16:00:00",
            2,
            "2020-06-14T15:00:00",
            "2020-06-14T18:30:00",
            25.45),
        Arguments.of(
            "Test 3: petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)",
            "2020-06-14T21:00:00",
            1,
            "2020-06-14T00:00:00",
            "2020-12-31T23:59:59",
            35.50),
        Arguments.of(
            "Test 4: petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)",
            "2020-06-15T10:00:00",
            3,
            "2020-06-15T00:00:00",
            "2020-06-15T11:00:00",
            30.50),
        Arguments.of(
            "Test 5: petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)",
            "2020-06-16T21:00:00",
            4,
            "2020-06-15T16:00:00",
            "2020-12-31T23:59:59",
            38.95));
  }

  @Nested
  class HappyPathTests {

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.inditex.apps.pricing.controller.prices.PricesGetApplicableControllerShould#happyPathScenarios")
    void appliesExpectedPrice(
        String displayName,
        String applicationDate,
        int priceList,
        String startDate,
        String endDate,
        double price)
        throws Exception {
      assertResponse(
          String.format(ENDPOINT, applicationDate),
          200,
          String.format(
              Locale.US,
              """
              {
                  "productId": 35455,
                  "brandId": 1,
                  "priceList": %d,
                  "startDate": "%s",
                  "endDate": "%s",
                  "price": %.2f,
                  "currency": "EUR"
              }
              """,
              priceList,
              startDate,
              endDate,
              price));
    }
  }

  @Nested
  class ErrorPathTests {

    @Test
    @DisplayName("Returns 400 with invalid_date_format when applicationDate is malformed")
    void returns400WhenApplicationDateIsMalformed() throws Exception {
      assertResponse(
          String.format(ENDPOINT, "not-a-date"),
          400,
          """
          {
              "status": 400,
              "errorCode": "invalid_date_format"
          }
          """);
    }

    @Test
    @DisplayName("Returns 404 with price_not_found when no price window matches the date")
    void returns404WhenNoPriceMatchesDate() throws Exception {
      assertResponse(
          String.format(ENDPOINT, "2019-01-01T00:00:00"),
          404,
          """
          {
              "status": 404,
              "errorCode": "price_not_found"
          }
          """);
    }

    @Test
    @DisplayName("Returns 400 without errorCode when a required parameter is missing")
    void returns400WithoutErrorCodeWhenParamsArePartial() throws Exception {
      assertStatusWithoutErrorCode("/api/v1/prices/find?brandId=1", 400);
    }

    @Test
    @DisplayName("Returns the applicable price without the id field")
    void omitsIdFromApplicablePrice() throws Exception {
      assertJsonPathAbsent(String.format(ENDPOINT, "2020-06-14T10:00:00"), "$.id");
    }
  }
}
