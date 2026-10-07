package com.inditex.pricing.shared.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

import com.inditex.pricing.shared.domain.bus.event.DomainEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AggregateRoot")
class AggregateRootTest {

  private static final class TestAggregate extends AggregateRoot {

    void recordEvent(DomainEvent event) {
      register(event);
    }
  }

  private static final class TestEvent extends DomainEvent {

    private TestEvent(String aggregateId) {
      super(aggregateId, "event-" + aggregateId, Instant.now());
    }

    @Override
    public String eventName() {
      return "test.event";
    }

    @Override
    public Map<String, Serializable> toPrimitives() {
      return Map.of();
    }
  }

  private TestAggregate aggregate;

  @BeforeEach
  void setUp() {
    aggregate = new TestAggregate();
  }

  @Nested
  @DisplayName("pullDomainEvents")
  class PullDomainEvents {

    @Test
    @DisplayName("returns an empty list when nothing was recorded")
    void returnsEmptyWhenNothingRecorded() {
      assertThat(aggregate.pullDomainEvents()).isEmpty();
    }

    @Test
    @DisplayName("returns recorded events in insertion order")
    void returnsRecordedEventsInOrder() {
      var eventA = new TestEvent("a");
      var eventB = new TestEvent("b");

      aggregate.recordEvent(eventA);
      aggregate.recordEvent(eventB);

      assertThat(aggregate.pullDomainEvents()).containsExactly(eventA, eventB);
    }

    @Test
    @DisplayName("returns an empty list on a second consecutive call")
    void returnsEmptyOnSecondConsecutiveCall() {
      aggregate.recordEvent(new TestEvent("a"));
      aggregate.pullDomainEvents();

      assertThat(aggregate.pullDomainEvents()).isEmpty();
    }

    @Test
    @DisplayName("returns an immutable list")
    void returnsImmutableList() {
      aggregate.recordEvent(new TestEvent("a"));
      var events = aggregate.pullDomainEvents();

      assertThatThrownBy(() -> events.add(new TestEvent("b")))
          .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("mutating the returned list does not affect a subsequent pull")
    void mutatingReturnedListDoesNotAffectSubsequentPull() {
      aggregate.recordEvent(new TestEvent("a"));
      var firstPull = aggregate.pullDomainEvents();

      assertThatThrownBy(() -> firstPull.clear()).isInstanceOf(UnsupportedOperationException.class);
      assertThat(aggregate.pullDomainEvents()).isEmpty();
    }
  }
}
