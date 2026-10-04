package com.inditex.apps.pricing.controller.prices.v1;

import java.util.Map;

import com.inditex.pricing.prices.application.PriceResponse;
import com.inditex.pricing.prices.application.find.FindApplicablePriceQuery;
import com.inditex.pricing.prices.domain.PriceNotFoundException;
import com.inditex.pricing.shared.domain.DomainError;
import com.inditex.pricing.shared.domain.InvalidDateFormat;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;
import com.inditex.pricing.shared.infrastructure.spring.ApiController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{version}")
public class PriceApplicableGetController extends ApiController {

  public PriceApplicableGetController(QueryBus queryBus) {
    super(queryBus);
  }

  @GetMapping(value = "/price", version = "v1")
  public ResponseEntity<PriceResponse> index(
      @RequestParam String applicationDate,
      @RequestParam Long productId,
      @RequestParam Long brandId) {
    PriceResponse priceResponse =
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
