package com.inditex.apps.pricing.controller.prices.v1.put;

import java.util.Map;

import com.inditex.pricing.prices.application.update.UpdatePriceCommand;
import com.inditex.pricing.prices.domain.exception.InvalidPriceAmount;
import com.inditex.pricing.prices.domain.exception.InvalidPriceCurrency;
import com.inditex.pricing.prices.domain.exception.InvalidPriceDateRange;
import com.inditex.pricing.prices.domain.exception.InvalidPriceReference;
import com.inditex.pricing.prices.domain.exception.PriceAlreadyExists;
import com.inditex.pricing.prices.domain.exception.PriceNotFoundException;
import com.inditex.pricing.shared.domain.bus.command.CommandBus;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;
import com.inditex.pricing.shared.domain.exception.DomainError;
import com.inditex.pricing.shared.domain.exception.FieldRequired;
import com.inditex.pricing.shared.domain.exception.InvalidDateFormat;
import com.inditex.pricing.shared.domain.exception.InvalidUUID;
import com.inditex.pricing.shared.infrastructure.spring.ApiController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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
      summary = "Replace a price",
      description =
          "Fully replaces the price identified by id;"
              + " responds 404 if it does not exist and 409 if the resulting business key already belongs to another price.")
  @ApiResponse(responseCode = "204", description = "Price replaced, no body")
  @ApiResponse(
      responseCode = "400",
      description =
          "invalid_uuid when the path id is not a UUID, or domain validation error with "
              + "errorCode (field_required, invalid_reference, invalid_price_amount, "
              + "invalid_price_date_range, invalid_price_currency, invalid_date_format), "
              + "or malformed body (no errorCode)")
  @ApiResponse(responseCode = "404", description = "price_not_found")
  @ApiResponse(responseCode = "409", description = "price_already_exists")
  @ApiResponse(responseCode = "500", description = "Unexpected error")
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
        InvalidPriceAmount.class, HttpStatus.BAD_REQUEST,
        InvalidPriceDateRange.class, HttpStatus.BAD_REQUEST,
        InvalidPriceCurrency.class, HttpStatus.BAD_REQUEST,
        FieldRequired.class, HttpStatus.BAD_REQUEST,
        InvalidDateFormat.class, HttpStatus.BAD_REQUEST);
  }
}
