package com.inditex.pricing.shared.infrastructure.bus.event;

import java.util.List;

import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.event.DomainEvent;
import com.inditex.pricing.shared.domain.bus.event.EventBus;

import org.springframework.context.ApplicationEventPublisher;

@Service
public final class SpringApplicationEventBus implements EventBus {

  private final ApplicationEventPublisher publisher;

  public SpringApplicationEventBus(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void publish(List<DomainEvent> events) {
    events.forEach(publisher::publishEvent);
  }
}
