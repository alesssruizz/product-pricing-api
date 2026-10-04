package com.inditex.apps.pricing.controller.prices.v1;

import com.inditex.pricing.prices.application.PricesResponse;
import com.inditex.pricing.prices.application.searchall.PriceSearchAllQuery;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;
import com.inditex.pricing.shared.infrastructure.spring.ApiController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{version}")
public class PriceGetAllController extends ApiController {

  public PriceGetAllController(QueryBus queryBus) {
    super(queryBus);
  }

  @GetMapping(value = "/prices", version = "v1")
  public ResponseEntity<PricesResponse> index() {
    PricesResponse prices = ask(new PriceSearchAllQuery());

    return ResponseEntity.ok().body(prices);
  }
}
