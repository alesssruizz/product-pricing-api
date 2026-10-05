package com.inditex.apps.pricing.controller.prices.v1.delete;

import java.util.Map;

import com.inditex.pricing.prices.application.delete.PriceDeleter;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.DomainError;
import com.inditex.pricing.shared.domain.InvalidUUID;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;
import com.inditex.pricing.shared.infrastructure.spring.ApiController;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Prices")
public class PricesDeleteController extends ApiController {

  private final PriceDeleter deleter;

  public PricesDeleteController(QueryBus queryBus, PriceDeleter deleter) {
    super(queryBus);
    this.deleter = deleter;
  }

  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Price deleted, no body"),
    @ApiResponse(
        responseCode = "400",
        description = "invalid_uuid",
        content =
            @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class))),
    @ApiResponse(
        responseCode = "404",
        description = "price_not_found",
        content =
            @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Unexpected error",
        content =
            @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)))
  })
  @DeleteMapping(value = "/prices/{id}", version = "v1")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    deleter.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public Map<Class<? extends DomainError>, HttpStatus> errorMapping() {
    return Map.of(
        PriceNotFoundException.class, HttpStatus.NOT_FOUND,
        InvalidUUID.class, HttpStatus.BAD_REQUEST);
  }
}
