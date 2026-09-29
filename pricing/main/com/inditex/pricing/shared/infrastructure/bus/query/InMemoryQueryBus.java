package com.inditex.pricing.shared.infrastructure.bus.query;

import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.query.Query;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;
import com.inditex.pricing.shared.domain.bus.query.QueryHandler;
import com.inditex.pricing.shared.domain.bus.query.QueryHandlerExecutionError;
import com.inditex.pricing.shared.domain.bus.query.Response;

@Service
public final class InMemoryQueryBus implements QueryBus {

    private final QueryHandlersInformation information;

    public InMemoryQueryBus(QueryHandlersInformation information) {
        this.information = information;
    }

    @Override
    public Response ask(Query query) throws QueryHandlerExecutionError {
        try {
            QueryHandler handler = information.search(query.getClass());

            return handler.handle(query);
        } catch (Throwable error) {
            throw new QueryHandlerExecutionError(error);
        }
    }
}
