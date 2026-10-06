package com.inditex.apps.pricing.controller.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.DomainError;
import com.inditex.pricing.shared.domain.bus.command.CommandHandlerExecutionError;
import com.inditex.pricing.shared.domain.bus.query.QueryHandlerExecutionError;
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
  }

  private ResponseEntity<ProblemDetail> handle(Exception exception) {
    return handler.handleDomainError(exception, handlerMethod);
  }

  private static PriceNotFoundException priceNotFound() {
    return new PriceNotFoundException(new PriceId("00000000-0000-0000-0000-000000000999"));
  }

  private static HandlerMethod handlerMappingPriceNotFoundTo404() {
    ApiController controller =
        new ApiController(null, null) {
          @Override
          public Map<Class<? extends DomainError>, HttpStatus> errorMapping() {
            return Map.of(PriceNotFoundException.class, HttpStatus.NOT_FOUND);
          }
        };
    try {
      return new HandlerMethod(controller, Object.class.getMethod("toString"));
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }
  }
}
