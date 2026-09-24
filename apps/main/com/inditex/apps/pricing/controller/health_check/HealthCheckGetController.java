package com.inditex.apps.pricing.controller.health_check;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inditex.pricing.Example;

@RestController
public final class HealthCheckGetController {

    @GetMapping("/health-check")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().body(Example.main());
        //		return new HashMap<>(){{
        //			put("application", "product-pricing-api");
        //			put("status", "ok");
        //		}};
    }
}
