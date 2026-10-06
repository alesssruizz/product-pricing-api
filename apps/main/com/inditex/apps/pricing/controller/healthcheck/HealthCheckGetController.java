package com.inditex.apps.pricing.controller.healthcheck;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health")
public final class HealthCheckGetController {

  @Operation(
      summary = "Comprobar el estado de la aplicación",
      description = "Devuelve 200 si la aplicación está levantada.")
  @ApiResponse(
      responseCode = "200",
      description = "Application is up",
      content =
          @Content(
              mediaType = "application/json",
              schema =
                  @Schema(
                      type = "object",
                      example = "{\"application\": \"product-pricing-api\", \"status\": \"ok\"}")))
  @GetMapping("/health-check")
  public ResponseEntity<Map<String, String>> healthCheck() {
    Map<String, String> status =
        Map.of(
            "application", "product-pricing-api",
            "status", "ok");

    return ResponseEntity.ok().body(status);
  }
}
