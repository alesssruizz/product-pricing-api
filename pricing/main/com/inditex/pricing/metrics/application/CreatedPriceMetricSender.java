package com.inditex.pricing.metrics.application;

import com.inditex.pricing.metrics.domain.Metric;
import com.inditex.pricing.metrics.domain.MetricSender;
import com.inditex.pricing.prices.domain.event.PriceCreatedDomainEvent;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.event.DomainEventSuscriber;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;

@Service
@AllArgsConstructor
@DomainEventSuscriber({PriceCreatedDomainEvent.class})
public class CreatedPriceMetricSender {
  private MetricSender sender;

  @EventListener
  public void on(PriceCreatedDomainEvent event) {
    sender.send(new Metric(event.eventName(), event.toPrimitives()));
  }
}
