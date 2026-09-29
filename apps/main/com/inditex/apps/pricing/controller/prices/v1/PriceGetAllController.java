package com.inditex.apps.pricing.controller.prices.v1;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inditex.pricing.prices.application.PricesResponse;
import com.inditex.pricing.prices.application.search_all.PriceSearchAllQuery;
import com.inditex.pricing.shared.domain.DomainError;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;
import com.inditex.pricing.shared.infrastructure.spring.ApiController;

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

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }
}
