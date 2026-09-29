package com.inditex.apps.pricing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.inditex.pricing.shared.domain.Service;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.inditex.pricing")
@EntityScan(basePackages = "com.inditex.pricing")
@ComponentScan(
    includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Service.class),
    value = { "com.inditex.pricing", "com.inditex.apps.pricing" }
)
public class ProductPricingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductPricingApiApplication.class, args);
    }
}
