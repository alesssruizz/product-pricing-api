package com.inditex.apps.pricing.controller.prices;

import org.junit.jupiter.api.Test;

import com.inditex.apps.pricing.ProductPricingApiApplicationTests;

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
                        "productId": 35455,
                        "brandId": 1,
                        "priceList": 1,
                        "startDate": "2020-06-14T00:00:00",
                        "endDate": "2020-12-31T23:59:59",
                        "price": 35.50,
                        "currency": "EUR"
                    },
                    {
                        "productId": 35455,
                        "brandId": 1,
                        "priceList": 2,
                        "startDate": "2020-06-14T15:00:00",
                        "endDate": "2020-06-14T18:30:00",
                        "price": 25.45,
                        "currency": "EUR"
                    },
                    {
                        "productId": 35455,
                        "brandId": 1,
                        "priceList": 3,
                        "startDate": "2020-06-15T00:00:00",
                        "endDate": "2020-06-15T11:00:00",
                        "price": 30.50,
                        "currency": "EUR"
                    },
                    {
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
            """
        );
    }
}
