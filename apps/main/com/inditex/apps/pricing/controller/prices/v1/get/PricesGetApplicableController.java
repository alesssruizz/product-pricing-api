package com.inditex.apps.pricing.controller.prices.v1.get;

import java.util.Map;

import com.inditex.pricing.prices.application.ApplicablePriceResponse;
import com.inditex.pricing.prices.application.find.FindApplicablePriceQuery;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.DomainError;
import com.inditex.pricing.shared.domain.InvalidDateFormat;
import com.inditex.pricing.shared.domain.bus.command.CommandBus;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;
import com.inditex.pricing.shared.infrastructure.spring.ApiController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Prices")
public class PricesGetApplicableController extends ApiController {

  public PricesGetApplicableController(QueryBus queryBus, CommandBus commandBus) {
    super(queryBus, commandBus);
  }

  @Operation(
      summary = "Obtener el precio aplicable",
      description =
          "Devuelve el precio aplicable para una marca, producto y fecha; la prioridad decide entre precios solapados.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Applicable price",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApplicablePriceResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description =
            "invalid_date_format when applicationDate is not ISO-8601, "
                + "or missing/mistyped parameter (no errorCode)",
        content =
            @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class))),
    @ApiResponse(
        responseCode = "404",
        description = "price_not_found: no price applies",
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
  @GetMapping(value = "/prices/find", version = "v1")
  public ResponseEntity<ApplicablePriceResponse> index(
      @RequestParam String applicationDate,
      @RequestParam Long productId,
      @RequestParam Long brandId) {

    ApplicablePriceResponse priceResponse =
        ask(new FindApplicablePriceQuery(brandId, productId, applicationDate));

    return ResponseEntity.ok().body(priceResponse);
  }

  @Override
  public Map<Class<? extends DomainError>, HttpStatus> errorMapping() {

    return Map.of(
        PriceNotFoundException.class, HttpStatus.NOT_FOUND,
        InvalidDateFormat.class, HttpStatus.BAD_REQUEST);
  }
}
