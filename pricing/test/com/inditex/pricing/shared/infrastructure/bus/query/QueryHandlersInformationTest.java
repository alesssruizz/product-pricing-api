package com.inditex.pricing.shared.infrastructure.bus.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import com.inditex.pricing.shared.domain.bus.query.DuplicateQueryHandlerError;
import com.inditex.pricing.shared.domain.bus.query.Query;
import com.inditex.pricing.shared.domain.bus.query.QueryHandler;
import com.inditex.pricing.shared.domain.bus.query.QueryNotRegisteredError;
import com.inditex.pricing.shared.domain.bus.query.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("QueryHandlersInformation")
@SuppressWarnings({"rawtypes", "unchecked"})
class QueryHandlersInformationTest {

  record FirstQuery() implements Query {}

  record SecondQuery() implements Query {}

  record UnknownQuery() implements Query {}

  record EmptyResponse() implements Response {}

  static class FirstQueryHandler implements QueryHandler<FirstQuery, EmptyResponse> {
    @Override
    public EmptyResponse handle(FirstQuery query) {
      return new EmptyResponse();
    }
  }

  static class SecondQueryHandler implements QueryHandler<SecondQuery, EmptyResponse> {
    @Override
    public EmptyResponse handle(SecondQuery query) {
      return new EmptyResponse();
    }
  }

  static class DuplicatedFirstQueryHandler implements QueryHandler<FirstQuery, EmptyResponse> {
    @Override
    public EmptyResponse handle(FirstQuery query) {
      return new EmptyResponse();
    }
  }

  static class RawQueryHandler implements QueryHandler {
    @Override
    public Response handle(Query query) {
      return new EmptyResponse();
    }
  }

  @Nested
  @DisplayName("Resolving handlers")
  class ResolvingScenarios {

    @Test
    @DisplayName("Indexes each handler by its query type")
    void resolvesHandlersByQueryType() throws QueryNotRegisteredError {
      var first = new FirstQueryHandler();
      var second = new SecondQueryHandler();
      var information = new QueryHandlersInformation(List.of(first, second));

      assertThat(information.search(FirstQuery.class)).isSameAs(first);
      assertThat(information.search(SecondQuery.class)).isSameAs(second);
    }

    @Test
    @DisplayName("Throws not registered for a query without handler")
    void throwsNotRegisteredForUnknownQuery() {
      var information = new QueryHandlersInformation(List.of(new FirstQueryHandler()));

      assertThatThrownBy(() -> information.search(UnknownQuery.class))
          .isInstanceOf(QueryNotRegisteredError.class);
    }
  }

  @Nested
  @DisplayName("Registration errors")
  class RegistrationErrorScenarios {

    @Test
    @DisplayName("Rejects two handlers for the same query type")
    void rejectsDuplicateHandlers() {
      List<QueryHandler> handlers =
          List.of(new FirstQueryHandler(), new DuplicatedFirstQueryHandler());

      assertThatThrownBy(() -> new QueryHandlersInformation(handlers))
          .isInstanceOf(DuplicateQueryHandlerError.class);
    }

    @Test
    @DisplayName("Rejects a raw handler whose query generic cannot be resolved")
    void rejectsUnresolvableGeneric() {
      List<QueryHandler> handlers = List.of(new RawQueryHandler());

      assertThatThrownBy(() -> new QueryHandlersInformation(handlers))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining(RawQueryHandler.class.getName());
    }
  }
}
