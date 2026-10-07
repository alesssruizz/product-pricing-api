package com.inditex.pricing.shared.domain.bus.query;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("QueryErrors")
class QueryErrorsTest {

  record SomeQuery() implements Query {}

  @Nested
  @DisplayName("QueryNotRegisteredError")
  class QueryNotRegisteredErrorScenarios {

    @Test
    @DisplayName("Message includes the query class")
    void messageIncludesQueryClass() {
      var error = new QueryNotRegisteredError(SomeQuery.class);

      assertThat(error.getMessage())
          .isEqualTo("No handler is registered for query <" + SomeQuery.class.toString() + ">");
    }
  }

  @Nested
  @DisplayName("DuplicateQueryHandlerError")
  class DuplicateQueryHandlerErrorScenarios {

    @Test
    @DisplayName("Is a RuntimeException")
    void isRuntimeException() {
      var error = new DuplicateQueryHandlerError(SomeQuery.class);

      assertThat(error).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Message includes the query class")
    void messageIncludesQueryClass() {
      var error = new DuplicateQueryHandlerError(SomeQuery.class);

      assertThat(error.getMessage())
          .isEqualTo(
              String.format("Duplicate QueryHandler registered for query <%s>", SomeQuery.class));
    }
  }
}
