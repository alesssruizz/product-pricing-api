package com.inditex.pricing.shared.domain.bus.query;

public final class DuplicateQueryHandlerError extends RuntimeException {

    public DuplicateQueryHandlerError(Class<? extends Query> query) {
        super(String.format("Duplicate QueryHandler registered for query <%s>", query));
    }
}
