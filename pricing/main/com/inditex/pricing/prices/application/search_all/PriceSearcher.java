package com.inditex.pricing.prices.application.search_all;

import com.inditex.pricing.prices.application.PriceResponse;
import com.inditex.pricing.prices.application.PricesResponse;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.shared.domain.Service;

@Service
public class PriceSearcher {

    private final PriceRepository repository;

    public PriceSearcher(PriceRepository repository) {
        this.repository = repository;
    }

    public PricesResponse search() {
        return new PricesResponse(
            repository.searchAll().stream().map(PriceResponse::fromAggregate).toList()
        );
    }
}
