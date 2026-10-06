package com.inditex.pricing.shared.domain.bus.query;

public final class QueryNotRegisteredError extends Exception {

  public QueryNotRegisteredError(Class<? extends Query> query) {
    super(String.format("No handler is registered for query <%s>", query.toString()));
  }
}
