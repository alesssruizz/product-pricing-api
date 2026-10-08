package com.inditex.apps.pricing.controller.prices.v1.post;

import java.net.URI;
import java.util.Map;

import com.inditex.pricing.prices.application.create.CreatePriceCommand;
import com.inditex.pricing.prices.domain.exception.InvalidPriceAmount;
import com.inditex.pricing.prices.domain.exception.InvalidPriceCurrency;
import com.inditex.pricing.prices.domain.exception.InvalidPriceDateRange;
import com.inditex.pricing.prices.domain.exception.InvalidPriceReference;
import com.inditex.pricing.prices.domain.exception.PriceAlreadyExists;
import com.inditex.pricing.prices.domain.exception.PriceIdAlreadyExists;
import com.inditex.pricing.shared.domain.bus.command.CommandBus;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;
import com.inditex.pricing.shared.domain.exception.DomainError;
import com.inditex.pricing.shared.domain.exception.FieldRequired;
import com.inditex.pricing.shared.domain.exception.InvalidDateFormat;
import com.inditex.pricing.shared.domain.exception.InvalidUUID;
import com.inditex.pricing.shared.infrastructure.spring.ApiController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
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
      summary = "Create a price",
      description =
          "Creates a price with a UUID id supplied by the client and returns 201 with the Location header;"
              + " responds 409 if the id already exists or if another price already has the same brand, product, priority and start date.")
  @ApiResponse(
      responseCode = "201",
      description = "Price created, no body",
      headers =
          @Header(
              name = "Location",
              description = "URI of the created price",
              schema = @Schema(type = "string", format = "uri")))
  @ApiResponse(
      responseCode = "400",
      description =
          "Domain validation error with errorCode (invalid_uuid, field_required, "
              + "invalid_reference, invalid_price_amount, invalid_price_date_range, "
              + "invalid_price_currency, invalid_date_format) or malformed body (no errorCode)")
  @ApiResponse(
      responseCode = "409",
      description =
          "price_id_already_exists (id taken) or price_already_exists (business key conflict)")
  @ApiResponse(responseCode = "500", description = "Unexpected error")
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
        InvalidPriceAmount.class, HttpStatus.BAD_REQUEST,
        InvalidPriceDateRange.class, HttpStatus.BAD_REQUEST,
        InvalidPriceCurrency.class, HttpStatus.BAD_REQUEST,
        FieldRequired.class, HttpStatus.BAD_REQUEST,
        InvalidDateFormat.class, HttpStatus.BAD_REQUEST);
  }
}
