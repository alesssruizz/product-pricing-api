package com.inditex.apps.pricing.controller.exception;

import static org.springframework.http.ProblemDetail.forStatusAndDetail;

import java.util.Map;

import com.inditex.pricing.shared.domain.DomainError;
import com.inditex.pricing.shared.domain.Utils;
import com.inditex.pricing.shared.domain.bus.command.CommandHandlerExecutionError;
import com.inditex.pricing.shared.domain.bus.command.DuplicateCommandHandlerError;
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

  private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

  private static final String UNEXPECTED_ERROR_DETAIL = "Unexpected error";

  @ExceptionHandler
  public ResponseEntity<ProblemDetail> handleDomainError(
      Exception exception, HandlerMethod handlerMethod) {
    Throwable error = unwrap(exception);
    HttpStatus status = statusFor(handlerMethod, error);

    if (status.is5xxServerError()) {
      log.error("Unhandled error in {}", handlerMethod, error);
    }

    String detail = status.is5xxServerError() ? UNEXPECTED_ERROR_DETAIL : error.getMessage();
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
    return isBusWrapper(exception) && exception.getCause() != null
        ? exception.getCause()
        : exception;
  }

  private boolean isBusWrapper(Exception exception) {
    return exception instanceof QueryHandlerExecutionError
        || exception instanceof CommandHandlerExecutionError
        || exception instanceof DuplicateCommandHandlerError;
  }

  private String errorCodeFor(Throwable error) {
    return (error instanceof DomainError domainError)
        ? domainError.errorCode()
        : Utils.toSnake(error.getClass().getSimpleName());
  }
}
