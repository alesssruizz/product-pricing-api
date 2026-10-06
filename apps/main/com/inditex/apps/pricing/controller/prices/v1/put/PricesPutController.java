package com.inditex.apps.pricing.controller.prices.v1.put;

import java.util.Map;

import com.inditex.pricing.prices.application.update.UpdatePriceCommand;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceCurrency;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceDateRange;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceQuantity;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceReference;
import com.inditex.pricing.prices.domain.exceptions.PriceAlreadyExists;
import com.inditex.pricing.prices.domain.exceptions.PriceFieldRequired;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.DomainError;
import com.inditex.pricing.shared.domain.InvalidDateFormat;
import com.inditex.pricing.shared.domain.InvalidUUID;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Prices")
public class PricesPutController extends ApiController {

  public PricesPutController(QueryBus queryBus, CommandBus commandBus) {
    super(queryBus, commandBus);
  }

  @Operation(
      summary = "Reemplazar un precio",
      description =
          "Reemplaza por completo el precio identificado por id;"
              + " responde 404 si no existe y 409 si la clave de negocio resultante ya pertenece a otro precio.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Price replaced, no body"),
    @ApiResponse(
        responseCode = "400",
        description = "Domain validation error (with errorCode) or malformed body (no errorCode)",
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
  @PutMapping(value = "/prices/{id}", version = "v1")
  public ResponseEntity<Void> update(
      @PathVariable String id, @RequestBody PricePutRequest request) {

    dispatch(
        new UpdatePriceCommand(
            id,
            request.brandId(),
            request.productId(),
            request.priceList(),
            request.priority(),
            request.startDate(),
            request.endDate(),
            request.price(),
            request.currency()));

    return ResponseEntity.noContent().build();
  }

  @Override
  public Map<Class<? extends DomainError>, HttpStatus> errorMapping() {

    return Map.of(
        PriceNotFoundException.class, HttpStatus.NOT_FOUND,
        PriceAlreadyExists.class, HttpStatus.CONFLICT,
        InvalidUUID.class, HttpStatus.BAD_REQUEST,
        InvalidPriceReference.class, HttpStatus.BAD_REQUEST,
        InvalidPriceQuantity.class, HttpStatus.BAD_REQUEST,
        InvalidPriceDateRange.class, HttpStatus.BAD_REQUEST,
        InvalidPriceCurrency.class, HttpStatus.BAD_REQUEST,
        PriceFieldRequired.class, HttpStatus.BAD_REQUEST,
        InvalidDateFormat.class, HttpStatus.BAD_REQUEST);
  }
}
