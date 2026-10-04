package com.inditex.apps.pricing.controller.exception;

import static org.springframework.http.ProblemDetail.forStatusAndDetail;

import java.util.Map;

import com.inditex.pricing.shared.domain.DomainError;
import com.inditex.pricing.shared.domain.Utils;
import com.inditex.pricing.shared.domain.bus.query.QueryHandlerExecutionError;
import com.inditex.pricing.shared.infrastructure.spring.ApiController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

  @ExceptionHandler
  public ResponseEntity<ProblemDetail> handleDomainError(
      Exception exception, HandlerMethod handlerMethod) {
    Throwable error = unwrap(exception);
    HttpStatus status = statusFor(handlerMethod, error);

    String detail = error.getMessage();
    if (status.is5xxServerError()) {
      LOGGER.error("Unhandled error in {}", handlerMethod, error);
      detail = "Unexpected error";
    }

    ProblemDetail problem = forStatusAndDetail(status, detail);
    problem.setProperty("errorCode", errorCodeFor(error));
    return ResponseEntity.status(status).body(problem);
  }

  private HttpStatus statusFor(HandlerMethod handlerMethod, Throwable error) {
    if (handlerMethod.getBean() instanceof ApiController controller) {
      Map<Class<? extends DomainError>, HttpStatus> mapping = controller.errorMapping();
      return mapping.getOrDefault(error.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

  private Throwable unwrap(Exception exception) {
    return (exception instanceof QueryHandlerExecutionError && exception.getCause() != null)
        ? exception.getCause()
        : exception;
  }

  private String errorCodeFor(Throwable error) {
    return (error instanceof DomainError domainError)
        ? domainError.errorCode()
        : Utils.toSnake(error.getClass().getSimpleName());
  }
}
