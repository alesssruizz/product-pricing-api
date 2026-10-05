package com.inditex.apps.pricing.controller.prices;

import com.inditex.apps.pricing.ProductPricingApiApplicationTests;

import org.junit.jupiter.api.Test;

public class PricesGetControllerShould extends ProductPricingApiApplicationTests {

  @Test
  public void returnAllSeededPrices() throws Exception {

    assertResponse(
        "/api/v1/prices",
        200,
        """
        {
            "prices": [
                {
                    "id": 1,
                    "productId": 35455,
                    "brandId": 1,
                    "priceList": 1,
                    "startDate": "2020-06-14T00:00:00",
                    "endDate": "2020-12-31T23:59:59",
                    "price": 35.50,
                    "currency": "EUR"
                },
                {
                    "id": 2,
                    "productId": 35455,
                    "brandId": 1,
                    "priceList": 2,
                    "startDate": "2020-06-14T15:00:00",
                    "endDate": "2020-06-14T18:30:00",
                    "price": 25.45,
                    "currency": "EUR"
                },
                {
                    "id": 3,
                    "productId": 35455,
                    "brandId": 1,
                    "priceList": 3,
                    "startDate": "2020-06-15T00:00:00",
                    "endDate": "2020-06-15T11:00:00",
                    "price": 30.50,
                    "currency": "EUR"
                },
                {
                    "id": 4,
                    "productId": 35455,
                    "brandId": 1,
                    "priceList": 4,
                    "startDate": "2020-06-15T16:00:00",
                    "endDate": "2020-12-31T23:59:59",
                    "price": 38.95,
                    "currency": "EUR"
                }
            ]
        }
        """);
  }
}
