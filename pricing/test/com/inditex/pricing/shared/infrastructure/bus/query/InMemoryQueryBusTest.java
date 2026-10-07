package com.inditex.pricing.shared.infrastructure.bus.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inditex.pricing.shared.domain.bus.query.Query;
import com.inditex.pricing.shared.domain.bus.query.QueryHandler;
import com.inditex.pricing.shared.domain.bus.query.QueryHandlerExecutionError;
import com.inditex.pricing.shared.domain.bus.query.QueryNotRegisteredError;
import com.inditex.pricing.shared.domain.bus.query.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("InMemoryQueryBus")
@SuppressWarnings({"rawtypes", "unchecked"})
class InMemoryQueryBusTest {

  record FindQuery() implements Query {}

  record FindResponse() implements Response {}

  @Mock private QueryHandlersInformation information;

  @Mock private QueryHandler handler;

  private InMemoryQueryBus bus;

  @BeforeEach
  void setUp() {
    bus = new InMemoryQueryBus(information);
  }

  @Nested
  @DisplayName("Successful dispatch")
  class SuccessScenarios {

    @Test
    @DisplayName("Returns the response of the registered handler")
    void returnsHandlerResponse() throws QueryNotRegisteredError {
      var query = new FindQuery();
      var expected = new FindResponse();
      when(information.search(FindQuery.class)).thenReturn(handler);
      when(handler.handle(query)).thenReturn(expected);

      Response response = bus.ask(query);

      assertThat(response).isSameAs(expected);
      verify(handler).handle(query);
    }
  }

  @Nested
  @DisplayName("Error wrapping")
  class ErrorScenarios {

    @Test
    @DisplayName("Wraps the not registered error when no handler exists")
    void wrapsNotRegistered() throws QueryNotRegisteredError {
      var query = new FindQuery();
      var cause = new QueryNotRegisteredError(FindQuery.class);
      when(information.search(FindQuery.class)).thenThrow(cause);

      assertThatThrownBy(() -> bus.ask(query))
          .isInstanceOf(QueryHandlerExecutionError.class)
          .hasCause(cause);
    }

    @Test
    @DisplayName("Wraps a runtime error thrown by the handler")
    void wrapsHandlerFailure() throws QueryNotRegisteredError {
      var query = new FindQuery();
      var cause = new IllegalStateException("boom");
      when(information.search(FindQuery.class)).thenReturn(handler);
      doThrow(cause).when(handler).handle(any());

      assertThatThrownBy(() -> bus.ask(query))
          .isInstanceOf(QueryHandlerExecutionError.class)
          .hasCause(cause);
    }
  }
}
