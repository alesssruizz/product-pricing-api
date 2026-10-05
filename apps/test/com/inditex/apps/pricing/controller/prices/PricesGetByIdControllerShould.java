package com.inditex.apps.pricing.controller.prices;

import com.inditex.apps.pricing.ProductPricingApiApplicationTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PricesGetByIdControllerShould extends ProductPricingApiApplicationTests {

  @Nested
  class HappyPathTests {

    @Test
    @DisplayName("Returns 200 with the price identified by id")
    void returnsThePriceById() throws Exception {
      assertResponse(
          "/api/v1/prices/2",
          200,
          """
          {
              "id": 2,
              "productId": 35455,
              "brandId": 1,
              "priceList": 2,
              "startDate": "2020-06-14T15:00:00",
              "endDate": "2020-06-14T18:30:00",
              "price": 25.45,
              "currency": "EUR"
          }
          """);
    }
  }

  @Nested
  class ErrorPathTests {

    @Test
    @DisplayName("Returns 404 with price_not_found when no price has the id")
    void returns404WhenPriceDoesNotExist() throws Exception {
      assertResponse(
          "/api/v1/prices/999",
          404,
          """
          {
              "status": 404,
              "errorCode": "price_not_found"
          }
          """);
    }
  }
}
