package com.inditex.apps.pricing.config;

import java.util.Objects;
import java.util.Set;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;

@Configuration
public class OpenApiConfiguration {

  private static final String PROBLEM_MEDIA_TYPE = "application/problem+json";

  private static final String PROBLEM_SCHEMA_REF = "#/components/schemas/ProblemDetail";

  @Bean
  public OpenApiCustomizer problemDetailErrorResponsesCustomizer() {
    return openApi -> {
      if (openApi.getComponents() == null) {
        openApi.setComponents(new Components());
      }
      ModelConverters.getInstance()
          .readAll(ProblemDetail.class)
          .forEach(openApi.getComponents()::addSchemas);

      if (openApi.getPaths() == null) {
        return;
      }
      openApi.getPaths().values().stream()
          .flatMap(pathItem -> pathItem.readOperations().stream())
          .map(Operation::getResponses)
          .filter(Objects::nonNull)
          .flatMap(responses -> responses.entrySet().stream())
          .filter(entry -> isErrorCode(entry.getKey()) && hasNoProblemContent(entry.getValue()))
          .forEach(entry -> entry.getValue().content(problemContent()));
    };
  }

  private static boolean isErrorCode(String code) {
    return code.startsWith("4") || code.startsWith("5");
  }

  private static boolean hasNoProblemContent(ApiResponse response) {
    Content content = response.getContent();
    return content == null || content.keySet().equals(Set.of("*/*"));
  }

  private static Content problemContent() {
    return new Content()
        .addMediaType(
            PROBLEM_MEDIA_TYPE, new MediaType().schema(new Schema<>().$ref(PROBLEM_SCHEMA_REF)));
  }
}
