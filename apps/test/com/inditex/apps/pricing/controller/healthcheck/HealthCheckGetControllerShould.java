package com.inditex.apps.pricing.controller.healthcheck;

import com.inditex.apps.pricing.ProductPricingApiApplicationTests;

import org.junit.jupiter.api.Test;

final class HealthCheckGetControllerShould extends ProductPricingApiApplicationTests {

  @Test
  public void check_the_app_is_working_ok() throws Exception {
    assertResponse("/health-check", 200, "{'application':'product-pricing-api','status':'ok'}");
  }
}
