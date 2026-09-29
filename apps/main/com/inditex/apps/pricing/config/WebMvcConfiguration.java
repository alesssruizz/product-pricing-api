package com.inditex.apps.pricing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.RequestPath;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private static final String HEALTH_CHECK_PACKAGE =
        "com.inditex.apps.pricing.controller.health_check";
    private static final String SPRINGDOC_PACKAGE = "org.springdoc";

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer.usePathSegment(1, this::isApiPath);
        configurer.setVersionRequired(false);
    }

    private boolean isApiPath(RequestPath path) {
        return path.pathWithinApplication().value().startsWith("/api/");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(
            "/api",
            HandlerTypePredicate
                .forAnnotation(RestController.class)
                .and(HandlerTypePredicate.forBasePackage(HEALTH_CHECK_PACKAGE).negate())
                .and(HandlerTypePredicate.forBasePackage(SPRINGDOC_PACKAGE).negate())
        );
    }
}
