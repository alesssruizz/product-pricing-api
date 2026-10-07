package com.inditex.pricing.shared.domain;

import java.util.ArrayList;
import java.util.List;

import com.inditex.pricing.shared.domain.bus.event.DomainEvent;

public abstract class AggregateRoot {

  private final List<DomainEvent> domainEvents = new ArrayList<>();

  protected final void register(DomainEvent event) {
    domainEvents.add(event);
  }

  public final List<DomainEvent> pullDomainEvents() {
    var events = List.copyOf(domainEvents);
    domainEvents.clear();
    return events;
  }
}
