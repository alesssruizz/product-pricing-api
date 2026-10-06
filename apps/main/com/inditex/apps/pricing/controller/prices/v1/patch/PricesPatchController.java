package com.inditex.apps.pricing.controller.prices.v1.patch;

import java.util.Map;

import com.inditex.pricing.prices.application.patch.PatchPriceCommand;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Prices")
public class PricesPatchController extends ApiController {

  public PricesPatchController(QueryBus queryBus, CommandBus commandBus) {
    super(queryBus, commandBus);
  }

  @Operation(
      summary = "Partially update a price",
      description =
          "Applies the submitted fields over the existing price and validates the resulting state;"
              + " responds 404 if it does not exist and 409 if the resulting business key already belongs to another price.")
  @ApiResponse(responseCode = "204", description = "Price patched, no body")
  @ApiResponse(
      responseCode = "400",
      description =
          "invalid_uuid when the path id is not a UUID, or domain validation error on the "
              + "merged state with errorCode (price_field_required, invalid_reference, "
              + "invalid_price_quantity, invalid_price_date_range, invalid_price_currency, "
              + "invalid_date_format), or malformed body (no errorCode)")
  @ApiResponse(responseCode = "404", description = "price_not_found")
  @ApiResponse(responseCode = "409", description = "price_already_exists")
  @ApiResponse(responseCode = "500", description = "Unexpected error")
  @PatchMapping(value = "/prices/{id}", version = "v1")
  public ResponseEntity<Void> patch(
      @PathVariable String id, @RequestBody PricePatchRequest request) {

    dispatch(
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
