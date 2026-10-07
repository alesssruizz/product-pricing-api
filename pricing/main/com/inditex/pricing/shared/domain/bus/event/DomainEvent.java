package com.inditex.pricing.shared.domain.bus.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public abstract class DomainEvent {

  private final String aggregateId;

  private final String eventId;

  private final Instant occurredOn;

  protected DomainEvent(String aggregateId) {
    this(aggregateId, UUID.randomUUID().toString(), Instant.now());
  }

  protected DomainEvent(String aggregateId, String eventId, Instant occurredOn) {
    this.aggregateId = aggregateId;
    this.eventId = eventId;
    this.occurredOn = occurredOn;
  }

  public String aggregateId() {
    return aggregateId;
  }

  public String eventId() {
    return eventId;
  }

  public Instant occurredOn() {
    return occurredOn;
  }

  public abstract String eventName();

  public abstract Map<String, Serializable> toPrimitives();
}
