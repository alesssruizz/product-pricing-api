package com.inditex.apps.pricing.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.function.Predicate;

import com.inditex.apps.pricing.controller.healthcheck.HealthCheckGetController;
import com.inditex.apps.pricing.controller.prices.v1.get.PricesGetController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.RequestPath;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

@ExtendWith(MockitoExtension.class)
@DisplayName("WebMvcConfiguration")
class WebMvcConfigurationTest {

  @Mock private ApiVersionConfigurer versionConfigurer;

  @Mock private PathMatchConfigurer pathMatchConfigurer;

  @Captor private ArgumentCaptor<Predicate<RequestPath>> pathPredicate;

  @Captor private ArgumentCaptor<Predicate<Class<?>>> controllerPredicate;

  private WebMvcConfiguration configuration;

  @BeforeEach
  void setUp() {
    configuration = new WebMvcConfiguration();
  }

  @Nested
  @DisplayName("API version detection")
  class VersioningScenarios {

    @Test
    @DisplayName("Treats only paths starting with /api/ as versioned API paths")
    void detectsOnlyApiPaths() {
      configuration.configureApiVersioning(versionConfigurer);
      verify(versionConfigurer).usePathSegment(eq(1), pathPredicate.capture());

      var isApiPath = pathPredicate.getValue();

      assertThat(isApiPath.test(RequestPath.parse("/api/v1/prices", ""))).isTrue();
      assertThat(isApiPath.test(RequestPath.parse("/apiary", ""))).isFalse();
      assertThat(isApiPath.test(RequestPath.parse("/health", ""))).isFalse();
    }

    @Test
    @DisplayName("Evaluates the path within the application, ignoring the context path")
    void ignoresContextPath() {
      configuration.configureApiVersioning(versionConfigurer);
      verify(versionConfigurer).usePathSegment(eq(1), pathPredicate.capture());

      assertThat(pathPredicate.getValue().test(RequestPath.parse("/ctx/api/v1", "/ctx"))).isTrue();
    }

    @Test
    @DisplayName("Does not require a version")
    void versionIsOptional() {
      configuration.configureApiVersioning(versionConfigurer);

      verify(versionConfigurer).setVersionRequired(false);
    }
  }

  @Nested
  @DisplayName("Path prefix")
  class PathPrefixScenarios {

    @Test
    @DisplayName("Applies the /api/{version} prefix to controllers outside excluded packages")
    void appliesPrefixOnlyToControllers() {
      configuration.configurePathMatch(pathMatchConfigurer);
      verify(pathMatchConfigurer)
          .addPathPrefix(eq("/api/{version}"), controllerPredicate.capture());

      var appliesToController = controllerPredicate.getValue();

      assertThat(appliesToController.test(PricesGetController.class)).isTrue();
      assertThat(appliesToController.test(HealthCheckGetController.class)).isFalse();
      assertThat(appliesToController.test(Object.class)).isFalse();
    }
  }
}
