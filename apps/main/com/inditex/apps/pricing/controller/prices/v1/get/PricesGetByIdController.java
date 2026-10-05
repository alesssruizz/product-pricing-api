package com.inditex.apps.pricing.controller.prices.v1.get;

import java.util.Map;

import com.inditex.pricing.prices.application.PriceResponse;
import com.inditex.pricing.prices.application.findbyid.FindPriceByIdQuery;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.DomainError;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Prices")
public class PricesGetByIdController extends ApiController {

  public PricesGetByIdController(QueryBus queryBus) {
    super(queryBus);
  }

  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Price found"),
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
  @GetMapping(value = "/prices/{id}", version = "v1")
  public ResponseEntity<PriceResponse> index(@PathVariable Long id) {
    PriceResponse price = ask(new FindPriceByIdQuery(id));
    return ResponseEntity.ok().body(price);
  }

  @Override
  public Map<Class<? extends DomainError>, HttpStatus> errorMapping() {
    return Map.of(PriceNotFoundException.class, HttpStatus.NOT_FOUND);
  }
}
