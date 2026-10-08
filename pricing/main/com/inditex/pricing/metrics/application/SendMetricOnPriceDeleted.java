package com.inditex.pricing.metrics.application;

import com.inditex.pricing.metrics.domain.Metric;
import com.inditex.pricing.metrics.domain.MetricSender;
import com.inditex.pricing.prices.domain.event.PriceDeletedDomainEvent;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.event.DomainEventSuscriber;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@Async
@Service
@AllArgsConstructor
@DomainEventSuscriber({PriceDeletedDomainEvent.class})
public class SendMetricOnPriceDeleted {
  private MetricSender sender;

  @EventListener
  public void on(PriceDeletedDomainEvent event) {
    sender.send(new Metric(event.eventName(), event.toPrimitives()));
  }
}
