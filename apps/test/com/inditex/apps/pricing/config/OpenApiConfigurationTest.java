package com.inditex.apps.pricing.config;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OpenApiConfiguration")
class OpenApiConfigurationTest {

  private static final String PATH = "/p";

  private static final String PROBLEM_MEDIA_TYPE = "application/problem+json";

  private static final String PROBLEM_SCHEMA_REF = "#/components/schemas/ProblemDetail";

  private OpenApiConfiguration configuration;

  @BeforeEach
  void setUp() {
    configuration = new OpenApiConfiguration();
  }

  @Nested
  @DisplayName("Components")
  class ComponentsScenarios {

    @Test
    @DisplayName("Creates components when absent and registers the ProblemDetail schema")
    void createsComponentsWithProblemDetailSchema() {
      var openApi = new OpenAPI();

      configuration.problemDetailErrorResponsesCustomizer().customise(openApi);

      assertThat(openApi.getComponents()).isNotNull();
      assertThat(openApi.getComponents().getSchemas()).containsKey("ProblemDetail");
    }

    @Test
    @DisplayName("Stops after the schema step when paths are null")
    void stopsWhenPathsAreNull() {
      var openApi = new OpenAPI().components(new Components());

      configuration.problemDetailErrorResponsesCustomizer().customise(openApi);

      assertThat(openApi.getPaths()).isNull();
      assertThat(openApi.getComponents().getSchemas()).containsKey("ProblemDetail");
    }
  }

  @Nested
  @DisplayName("Error responses")
  class ErrorResponseScenarios {

    @Test
    @DisplayName("Gives problem content to 4xx and 5xx with no content or only */*")
    void addsProblemContentToEmptyOrWildcardErrors() {
      var openApi = new OpenAPI();
      var responses =
          new ApiResponses()
              .addApiResponse("400", new ApiResponse())
              .addApiResponse("500", responseWithContent("*/*"));
      openApi.paths(pathWithResponses(responses));

      configuration.problemDetailErrorResponsesCustomizer().customise(openApi);

      assertProblemContent(responseAt(openApi, "400"));
      assertProblemContent(responseAt(openApi, "500"));
    }

    @Test
    @DisplayName("Preserves existing application/json error content")
    void preservesExistingErrorContent() {
      var openApi = new OpenAPI();
      var original = responseWithContent("application/json");
      var responses = new ApiResponses().addApiResponse("404", original);
      openApi.paths(pathWithResponses(responses));

      configuration.problemDetailErrorResponsesCustomizer().customise(openApi);

      assertThat(responseAt(openApi, "404").getContent()).isSameAs(original.getContent());
    }

    @Test
    @DisplayName("Preserves mixed */* plus another media type content")
    void preservesMixedWildcardContent() {
      var openApi = new OpenAPI();
      var original =
          new Content()
              .addMediaType("*/*", new MediaType())
              .addMediaType("application/json", new MediaType());
      var response = new ApiResponse().content(original);
      var responses = new ApiResponses().addApiResponse("400", response);
      openApi.paths(pathWithResponses(responses));

      configuration.problemDetailErrorResponsesCustomizer().customise(openApi);

      assertThat(responseAt(openApi, "400").getContent()).isSameAs(original);
    }
  }

  @Nested
  @DisplayName("Non-error responses")
  class NonErrorResponseScenarios {

    @Test
    @DisplayName("Leaves 2xx and default responses untouched")
    void leavesSuccessAndDefaultResponsesUntouched() {
      var openApi = new OpenAPI();
      var responses =
          new ApiResponses()
              .addApiResponse("200", new ApiResponse())
              .addApiResponse("default", new ApiResponse());
      openApi.paths(pathWithResponses(responses));

      configuration.problemDetailErrorResponsesCustomizer().customise(openApi);

      assertThat(responseAt(openApi, "200").getContent()).isNull();
      assertThat(responseAt(openApi, "default").getContent()).isNull();
    }

    @Test
    @DisplayName("Skips operations whose responses are null")
    void skipsOperationsWithNullResponses() {
      var openApi = new OpenAPI();
      var operation = new Operation();
      openApi.paths(new Paths().addPathItem(PATH, new PathItem().get(operation)));

      configuration.problemDetailErrorResponsesCustomizer().customise(openApi);

      assertThat(operation.getResponses()).isNull();
    }
  }

  private static ApiResponse responseWithContent(String mediaType) {
    return new ApiResponse().content(new Content().addMediaType(mediaType, new MediaType()));
  }

  private static Paths pathWithResponses(ApiResponses responses) {
    return new Paths().addPathItem(PATH, new PathItem().get(new Operation().responses(responses)));
  }

  private static ApiResponse responseAt(OpenAPI openApi, String code) {
    return openApi.getPaths().get(PATH).getGet().getResponses().get(code);
  }

  private static void assertProblemContent(ApiResponse response) {
    assertThat(response.getContent().keySet()).containsExactly(PROBLEM_MEDIA_TYPE);
    assertThat(response.getContent().get(PROBLEM_MEDIA_TYPE).getSchema().get$ref())
        .isEqualTo(PROBLEM_SCHEMA_REF);
  }
}
