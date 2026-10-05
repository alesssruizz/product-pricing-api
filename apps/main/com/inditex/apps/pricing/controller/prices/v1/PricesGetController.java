package com.inditex.apps.pricing.controller.prices.v1;

import com.inditex.pricing.prices.application.PricesResponse;
import com.inditex.pricing.prices.application.searchall.PriceSearchAllQuery;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;
import com.inditex.pricing.shared.infrastructure.spring.ApiController;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Prices")
public class PricesGetController extends ApiController {

  public PricesGetController(QueryBus queryBus) {
    super(queryBus);
  }

  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "All prices ordered by id"),
    @ApiResponse(
        responseCode = "500",
        description = "Unexpected error",
        content =
            @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)))
  })
  @GetMapping(value = "/prices", version = "v1")
  public ResponseEntity<PricesResponse> index() {
    PricesResponse prices = ask(new PriceSearchAllQuery());

    return ResponseEntity.ok().body(prices);
  }
}
