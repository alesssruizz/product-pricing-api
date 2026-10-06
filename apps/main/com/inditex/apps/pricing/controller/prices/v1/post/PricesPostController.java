package com.inditex.apps.pricing.controller.prices.v1.post;

import java.net.URI;
import java.util.Map;

import com.inditex.pricing.prices.application.create.CreatePriceCommand;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceCurrency;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceDateRange;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceQuantity;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceReference;
import com.inditex.pricing.prices.domain.exceptions.PriceAlreadyExists;
import com.inditex.pricing.prices.domain.exceptions.PriceFieldRequired;
import com.inditex.pricing.prices.domain.exceptions.PriceIdAlreadyExists;
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
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Tag(name = "Prices")
public class PricesPostController extends ApiController {

  public PricesPostController(QueryBus queryBus, CommandBus commandBus) {
    super(queryBus, commandBus);
  }

  @Operation(
      summary = "Crear un precio",
      description =
          "Crea un precio con id UUID aportado por el cliente y devuelve 201 con la cabecera Location;"
              + " responde 409 si el id ya existe o si ya hay otro precio con la misma marca, producto, prioridad y fecha de inicio.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Price created, Location header set"),
    @ApiResponse(
        responseCode = "400",
        description =
            "Domain validation error with errorCode (invalid_uuid, price_field_required, "
                + "invalid_reference, invalid_price_quantity, invalid_price_date_range, "
                + "invalid_price_currency, invalid_date_format) or malformed body (no errorCode)",
        content =
            @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class))),
    @ApiResponse(
        responseCode = "409",
        description =
            "price_id_already_exists (id taken) or price_already_exists (business key conflict)",
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
  @PostMapping(value = "/prices", version = "v1")
  public ResponseEntity<Void> create(@RequestBody PricePostRequest request) {

    dispatch(
        new CreatePriceCommand(
            request.id(),
            request.brandId(),
            request.productId(),
            request.priceList(),
            request.priority(),
            request.startDate(),
            request.endDate(),
            request.price(),
            request.currency()));

    return ResponseEntity.created(uriLocation(request.id())).build();
  }

  private @NonNull URI uriLocation(String id) {

    return ServletUriComponentsBuilder.fromCurrentRequestUri()
        .path("/{id}")
        .buildAndExpand(id)
        .toUri();
  }

  @Override
  public Map<Class<? extends DomainError>, HttpStatus> errorMapping() {

    return Map.of(
        PriceIdAlreadyExists.class, HttpStatus.CONFLICT,
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
