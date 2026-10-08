package com.inditex.apps.pricing.controller.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.bus.command.Command;
import com.inditex.pricing.shared.domain.bus.command.CommandHandlerExecutionError;
import com.inditex.pricing.shared.domain.bus.command.DuplicateCommandHandlerError;
import com.inditex.pricing.shared.domain.bus.query.DuplicateQueryHandlerError;
import com.inditex.pricing.shared.domain.bus.query.Query;
import com.inditex.pricing.shared.domain.bus.query.QueryHandlerExecutionError;
import com.inditex.pricing.shared.domain.exception.DomainError;
import com.inditex.pricing.shared.infrastructure.spring.ApiController;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.HandlerMethod;

class ApiExceptionHandlerShould {

  private final ApiExceptionHandler handler = new ApiExceptionHandler();

  private final HandlerMethod handlerMethod = handlerMappingPriceNotFoundTo404();

  static class TestError extends DomainError {
    TestError(String message) {
      super(message, "test_error");
    }
  }

  static class ChildTestError extends TestError {
    ChildTestError(String message) {
      super(message);
    }
  }

  @Nested
  class UnwrapTests {

    @Test
    @DisplayName("Returns the mapped status and errorCode for a CommandHandlerExecutionError")
    void mapsTheCauseOfACommandHandlerExecutionError() {
      ResponseEntity<ProblemDetail> response =
          handle(new CommandHandlerExecutionError(priceNotFound()));

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
      assertThat(response.getBody().getProperties()).containsEntry("errorCode", "price_not_found");
    }

    @Test
    @DisplayName("Returns the mapped status and errorCode for a QueryHandlerExecutionError")
    void mapsTheCauseOfAQueryHandlerExecutionError() {
      ResponseEntity<ProblemDetail> response =
          handle(new QueryHandlerExecutionError(priceNotFound()));

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
      assertThat(response.getBody().getProperties()).containsEntry("errorCode", "price_not_found");
    }

    @Test
    @DisplayName("Unwraps DuplicateCommandHandlerError and uses the cause for status and code")
    void unwrapsDuplicateCommandHandlerError() {
      var error = new DuplicateCommandHandlerError(Command.class);
      error.initCause(priceNotFound());

      ResponseEntity<ProblemDetail> response = handle(error);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
      assertThat(response.getBody().getProperties()).containsEntry("errorCode", "price_not_found");
    }

    @Test
    @DisplayName(
        "PINNED: does not unwrap DuplicateQueryHandlerError, unlike DuplicateCommandHandlerError")
    void doesNotUnwrapDuplicateQueryHandlerErrorPinned() {
      var error = new DuplicateQueryHandlerError(Query.class);
      error.initCause(priceNotFound());

      ResponseEntity<ProblemDetail> response = handle(error);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
      assertThat(response.getBody().getProperties())
          .containsEntry("errorCode", "duplicate_query_handler_error");
    }

    @Test
    @DisplayName("Does not unwrap a QueryHandlerExecutionError with a null cause")
    void doesNotUnwrapNullCause() {
      ResponseEntity<ProblemDetail> response =
          handle(new QueryHandlerExecutionError((Throwable) null));

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
      assertThat(response.getBody().getProperties())
          .containsEntry("errorCode", "query_handler_execution_error");
    }
  }

  @Nested
  class StatusResolution {

    @Test
    @DisplayName("Maps a non-ApiController bean to 500 and snake_case code")
    void nonApiControllerBeanMapsToInternalError() {
      ResponseEntity<ProblemDetail> response =
          handle(new IllegalStateException("boom"), handlerMethodFor(new Object()));

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
      assertThat(response.getBody().getDetail()).isEqualTo("boom");
      assertThat(response.getBody().getProperties())
          .containsEntry("errorCode", "illegal_state_exception");
    }

    @Test
    @DisplayName("Maps an unmapped DomainError to 500 with its own errorCode")
    void unmappedDomainErrorMapsToInternalError() {
      ResponseEntity<ProblemDetail> response = handle(new TestError("secret"));

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
      assertThat(response.getBody().getDetail()).isEqualTo("secret");
      assertThat(response.getBody().getProperties()).containsEntry("errorCode", "test_error");
    }

    @Test
    @DisplayName("Exposes the message for a mapped 4xx status")
    void mapped4xxExposesMessage() {
      var error = priceNotFound();

      ResponseEntity<ProblemDetail> response = handle(error);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
      assertThat(response.getBody().getDetail()).isEqualTo(error.getMessage());
      assertThat(response.getBody().getProperties()).containsEntry("errorCode", "price_not_found");
    }

    @Test
    @DisplayName("Falls back to 500 for a subclass of a mapped error (exact class match only)")
    void subclassOfMappedErrorFallsBackToInternalError() {
      var mapping = handlerMethodMapping(Map.of(TestError.class, HttpStatus.NOT_FOUND));

      ResponseEntity<ProblemDetail> response = handle(new ChildTestError("x"), mapping);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Nested
  class ErrorCodeTests {

    @Test
    @DisplayName("Uses the snake_case simple name as errorCode for a non-DomainError")
    void nonDomainErrorGetsSnakeCaseCode() {
      ResponseEntity<ProblemDetail> response = handle(new IllegalArgumentException("bad"));

      assertThat(response.getBody().getProperties())
          .containsEntry("errorCode", "illegal_argument_exception");
    }
  }

  private ResponseEntity<ProblemDetail> handle(Exception exception) {
    return handle(exception, handlerMethod);
  }

  private ResponseEntity<ProblemDetail> handle(Exception exception, HandlerMethod method) {
    return handler.handleDomainError(exception, method);
  }

  private static PriceNotFoundException priceNotFound() {
    return new PriceNotFoundException(new PriceId("00000000-0000-0000-0000-000000000999"));
  }

  private static HandlerMethod handlerMappingPriceNotFoundTo404() {
    return handlerMethodMapping(Map.of(PriceNotFoundException.class, HttpStatus.NOT_FOUND));
  }

  private static HandlerMethod handlerMethodMapping(
      Map<Class<? extends DomainError>, HttpStatus> mapping) {
    ApiController controller =
        new ApiController(null, null) {
          @Override
          public Map<Class<? extends DomainError>, HttpStatus> errorMapping() {
            return mapping;
          }
        };
    return handlerMethodFor(controller);
  }

  private static HandlerMethod handlerMethodFor(Object bean) {
    try {
      return new HandlerMethod(bean, Object.class.getMethod("toString"));
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }
  }
}
