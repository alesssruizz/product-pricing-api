package com.inditex.apps.pricing.controller.prices.v1;

import java.util.Map;

import com.inditex.pricing.prices.application.PriceResponse;
import com.inditex.pricing.prices.application.patch.PatchPriceCommand;
import com.inditex.pricing.prices.application.patch.PricePatcher;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceCurrency;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceDateRange;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceQuantity;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceReference;
import com.inditex.pricing.prices.domain.exceptions.PriceAlreadyExists;
import com.inditex.pricing.prices.domain.exceptions.PriceFieldRequired;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.DomainError;
import com.inditex.pricing.shared.domain.InvalidDateFormat;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Prices")
public class PricesPatchController extends ApiController {

  private final PricePatcher patcher;

  public PricesPatchController(QueryBus queryBus, PricePatcher patcher) {
    super(queryBus);
    this.patcher = patcher;
  }

  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Price patched"),
    @ApiResponse(
        responseCode = "400",
        description =
            "Domain validation error on the merged state (with errorCode) or malformed body (no errorCode)",
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
        responseCode = "409",
        description = "price_already_exists",
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
  @PatchMapping(value = "/prices/{id}", version = "v1")
  public ResponseEntity<PriceResponse> patch(
      @PathVariable Long id, @RequestBody PricePatchRequest request) {
    PriceResponse price =
        patcher.patch(
            new PatchPriceCommand(
                id,
                request.brandId(),
                request.productId(),
                request.priceList(),
                request.priority(),
                request.startDate(),
                request.endDate(),
                request.price(),
                request.currency()));

    return ResponseEntity.ok(price);
  }

  @Override
  public Map<Class<? extends DomainError>, HttpStatus> errorMapping() {
    return Map.of(
        PriceNotFoundException.class, HttpStatus.NOT_FOUND,
        PriceAlreadyExists.class, HttpStatus.CONFLICT,
        InvalidPriceReference.class, HttpStatus.BAD_REQUEST,
        InvalidPriceQuantity.class, HttpStatus.BAD_REQUEST,
        InvalidPriceDateRange.class, HttpStatus.BAD_REQUEST,
        InvalidPriceCurrency.class, HttpStatus.BAD_REQUEST,
        PriceFieldRequired.class, HttpStatus.BAD_REQUEST,
        InvalidDateFormat.class, HttpStatus.BAD_REQUEST);
  }
}
