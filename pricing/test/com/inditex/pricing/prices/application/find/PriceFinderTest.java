package com.inditex.pricing.prices.application.find;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inditex.pricing.prices.domain.PriceRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("PriceFinder")
class PriceFinderTest {

    @Mock
    private PriceRepository repository;

    private PriceFinder finder;

    @BeforeEach
    void setUp() {
        finder = new PriceFinder(repository);
    }

    @Nested
    @DisplayName("Test 1: example")
    class WhenPriceExists {

        @Test
        void returnsThePriceFromTheRepository() {}
    }
}
