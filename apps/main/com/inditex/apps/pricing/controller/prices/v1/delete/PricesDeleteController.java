package com.inditex.apps.pricing.controller.prices.v1.delete;

import java.util.Map;

import com.inditex.pricing.prices.application.delete.DeletePriceCommand;
import com.inditex.pricing.prices.domain.exception.PriceNotFoundException;
import com.inditex.pricing.shared.domain.bus.command.CommandBus;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;
import com.inditex.pricing.shared.domain.exception.DomainError;
import com.inditex.pricing.shared.domain.exception.InvalidUUID;
import com.inditex.pricing.shared.infrastructure.spring.ApiController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Prices")
public class PricesDeleteController extends ApiController {

  public PricesDeleteController(QueryBus queryBus, CommandBus commandBus) {
    super(queryBus, commandBus);
  }

  @Operation(
      summary = "Delete a price",
      description = "Deletes the price identified by id; responds 404 if it does not exist.")
  @ApiResponse(responseCode = "204", description = "Price deleted, no body")
  @ApiResponse(responseCode = "400", description = "invalid_uuid")
  @ApiResponse(responseCode = "404", description = "price_not_found")
  @ApiResponse(responseCode = "500", description = "Unexpected error")
  @DeleteMapping(value = "/prices/{id}", version = "v1")
  public ResponseEntity<Void> delete(@PathVariable String id) {

    dispatch(new DeletePriceCommand(id));

    return ResponseEntity.noContent().build();
  }

  @Override
  public Map<Class<? extends DomainError>, HttpStatus> errorMapping() {

    return Map.of(
        PriceNotFoundException.class, HttpStatus.NOT_FOUND,
        InvalidUUID.class, HttpStatus.BAD_REQUEST);
  }
}
