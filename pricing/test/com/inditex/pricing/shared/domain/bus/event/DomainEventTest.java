package com.inditex.pricing.shared.domain.bus.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("DomainEvent")
class DomainEventTest {

  private static final String AGGREGATE_ID = "3f1c2b4e-8a6d-4c1e-9f2a-7b5d0e9c1a23";

  private static final class TestDomainEvent extends DomainEvent {

    private TestDomainEvent(String aggregateId) {
      super(aggregateId);
    }

    private TestDomainEvent(String aggregateId, String eventId, Instant occurredOn) {
      super(aggregateId, eventId, occurredOn);
    }

    @Override
    public String eventName() {
      return "test.domain.event";
    }

    @Override
    public Map<String, Serializable> toPrimitives() {
      return Map.of();
    }
  }

  @Nested
  @DisplayName("generating constructor")
  class GeneratingConstructor {

    private Instant before;

    private Instant after;

    private TestDomainEvent event;

    @BeforeEach
    void setUp() {
      before = Instant.now();
      event = new TestDomainEvent(AGGREGATE_ID);
      after = Instant.now();
    }

    @Test
    @DisplayName("preserves the given aggregateId")
    void preservesAggregateId() {
      assertThat(event.aggregateId()).isEqualTo(AGGREGATE_ID);
    }

    @Test
    @DisplayName("generates an eventId that is a valid UUID")
    void generatesValidUuid() {
      assertThatCode(() -> UUID.fromString(event.eventId())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("sets occurredOn between before and after the construction")
    void setsOccurredOnWithinBounds() {
      assertThat(event.occurredOn()).isNotNull();
      assertThat(event.occurredOn()).isBetween(before, after);
    }

    @Test
    @DisplayName("generates distinct eventIds across instances")
    void generatesDistinctEventIds() {
      var other = new TestDomainEvent(AGGREGATE_ID);

      assertThat(event.eventId()).isNotEqualTo(other.eventId());
    }
  }

  @Nested
  @DisplayName("explicit constructor")
  class ExplicitConstructor {

    @Test
    @DisplayName("preserves aggregateId, eventId and occurredOn exactly as given")
    void preservesAllValues() {
      var eventId = "9e2f7c3a-1b4d-4e5f-8a6b-2c3d4e5f6a7b";
      var occurredOn = Instant.parse("2026-01-01T00:00:00Z");

      var event = new TestDomainEvent(AGGREGATE_ID, eventId, occurredOn);

      assertThat(event.aggregateId()).isEqualTo(AGGREGATE_ID);
      assertThat(event.eventId()).isEqualTo(eventId);
      assertThat(event.occurredOn()).isEqualTo(occurredOn);
    }
  }

  @Nested
  @DisplayName("eventName")
  class EventName {

    @Test
    @DisplayName("returns the value provided by the subclass")
    void returnsSubclassValue() {
      var event = new TestDomainEvent(AGGREGATE_ID);

      assertThat(event.eventName()).isEqualTo("test.domain.event");
    }
  }
}
