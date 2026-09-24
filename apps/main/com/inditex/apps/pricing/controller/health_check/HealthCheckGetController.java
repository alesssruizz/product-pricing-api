package com.inditex.apps.pricing.controller.health_check;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class HealthCheckGetController {

    @GetMapping("/health-check")
    public ResponseEntity<HashMap<String, String>> healthCheck() {
        HashMap<String, String> status = new HashMap<>() {
            {
                put("application", "product-pricing-api");
                put("status", "ok");
            }
        };

        return ResponseEntity.ok().body(status);
    }
}
