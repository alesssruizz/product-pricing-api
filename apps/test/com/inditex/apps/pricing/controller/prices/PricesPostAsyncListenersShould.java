package com.inditex.apps.pricing.controller.prices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.inditex.apps.pricing.ProductPricingApiApplicationTests;
import com.inditex.pricing.metrics.domain.Metric;
import com.inditex.pricing.metrics.domain.MetricSender;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PricesPostAsyncListenersShould extends ProductPricingApiApplicationTests {

  private static final String ENDPOINT = "/api/v1/prices";

  private static final String NEW_ID = "00000000-0000-0000-0000-000000000009";

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

  @MockitoBean private MetricSender metricSender;

  @Test
  @DisplayName("Returns 201 and persists the price even when a listener fails")
  void createsThePriceWhenAListenerFails() throws Exception {
    doThrow(new RuntimeException("metrics down")).when(metricSender).send(any());

    postBody(ENDPOINT, VALID_BODY).andExpect(status().isCreated());

    verify(metricSender, timeout(2000)).send(any(Metric.class));
    perform(get(ENDPOINT + "/" + NEW_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(NEW_ID));
  }

  @Test
  @DisplayName("Runs the listeners outside the request thread")
  void runsTheListenersInAnotherThread() throws Exception {
    var listenerThread = new CompletableFuture<Thread>();
    doAnswer(
            invocation -> {
              listenerThread.complete(Thread.currentThread());
              return null;
            })
        .when(metricSender)
        .send(any());

    postBody(ENDPOINT, VALID_BODY).andExpect(status().isCreated());

    assertThat(listenerThread.get(2, TimeUnit.SECONDS)).isNotSameAs(Thread.currentThread());
  }
}
