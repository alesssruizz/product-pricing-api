package com.inditex.pricing.metrics.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import com.inditex.pricing.metrics.domain.Metric;
import com.inditex.pricing.metrics.domain.MetricSender;
import com.inditex.pricing.prices.domain.event.PriceCreatedDomainEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("SendMetricOnPriceCreated")
class SendMetricOnPriceCreatedTest {

  @Mock private MetricSender sender;

  private SendMetricOnPriceCreated listener;

  @BeforeEach
  void setUp() {
    listener = new SendMetricOnPriceCreated(sender);
  }

  @Test
  @DisplayName("sends a metric named after the event with its primitives")
  void sendsTheMetric() {
    var event =
        new PriceCreatedDomainEvent(
            "00000000-0000-0000-0000-000000000009",
            1L,
            35455L,
            1,
            0,
            "2020-06-14T00:00:00",
            "2020-12-31T23:59:59",
            new BigDecimal("35.50"),
            "EUR");
    var metric = ArgumentCaptor.forClass(Metric.class);

    listener.on(event);

    verify(sender).send(metric.capture());
    assertThat(metric.getValue().message()).isEqualTo("price.created");
    assertThat(metric.getValue().json()).isEqualTo(event.toPrimitives());
  }
}
