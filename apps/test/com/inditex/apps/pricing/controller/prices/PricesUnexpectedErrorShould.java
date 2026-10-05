package com.inditex.apps.pricing.controller.prices;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inditex.apps.pricing.ProductPricingApiApplicationTests;
import com.inditex.pricing.prices.domain.PriceRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class PricesUnexpectedErrorShould extends ProductPricingApiApplicationTests {

  @MockitoBean private PriceRepository repository;

  @Test
  @DisplayName("Returns 500 with detail Unexpected error and errorCode for unhandled exceptions")
  public void returnUnexpectedErrorWhenRepositoryFails() throws Exception {
    when(repository.findAll()).thenThrow(new RuntimeException("connection lost"));

    perform(get("/api/v1/prices"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.detail").value("Unexpected error"))
        .andExpect(jsonPath("$.errorCode").value("runtime_exception"));
  }
}
