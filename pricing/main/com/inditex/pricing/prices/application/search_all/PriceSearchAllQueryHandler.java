package com.inditex.pricing.prices.application.search_all;

import com.inditex.pricing.prices.application.PricesResponse;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.query.QueryHandler;

@Service
public class PriceSearchAllQueryHandler
    implements QueryHandler<PriceSearchAllQuery, PricesResponse> {

    private final PriceSearcher searcher;

    public PriceSearchAllQueryHandler(PriceSearcher searcher) {
        this.searcher = searcher;
    }

    @Override
    public PricesResponse handle(PriceSearchAllQuery query) {
        return searcher.search();
    }
}
