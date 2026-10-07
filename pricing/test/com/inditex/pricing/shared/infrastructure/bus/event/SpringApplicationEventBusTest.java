package com.inditex.pricing.shared.infrastructure.bus.event;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoInteractions;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.inditex.pricing.shared.domain.bus.event.DomainEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
@DisplayName("SpringApplicationEventBus")
class SpringApplicationEventBusTest {

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

  @Mock private ApplicationEventPublisher publisher;

  private SpringApplicationEventBus bus;

  @BeforeEach
  void setUp() {
    bus = new SpringApplicationEventBus(publisher);
  }

  @Nested
  @DisplayName("publish")
  class Publish {

    @Test
    @DisplayName("publishes every event in order")
    void publishesEventsInOrder() {
      var eventA = new TestEvent("a");
      var eventB = new TestEvent("b");
      var eventC = new TestEvent("c");

      bus.publish(List.of(eventA, eventB, eventC));

      var order = inOrder(publisher);
      order.verify(publisher).publishEvent((Object) eventA);
      order.verify(publisher).publishEvent((Object) eventB);
      order.verify(publisher).publishEvent((Object) eventC);
    }

    @Test
    @DisplayName("does nothing when the list is empty")
    void doesNothingWhenListIsEmpty() {
      bus.publish(List.of());

      verifyNoInteractions(publisher);
    }
  }
}
