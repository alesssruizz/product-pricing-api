package com.inditex.apps.pricing.controller.healthcheck;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class HealthCheckGetController {

  @GetMapping("/health-check")
  public ResponseEntity<Map<String, String>> healthCheck() {
    Map<String, String> status =
        Map.of(
            "application", "product-pricing-api",
            "status", "ok");

    return ResponseEntity.ok().body(status);
  }
}
