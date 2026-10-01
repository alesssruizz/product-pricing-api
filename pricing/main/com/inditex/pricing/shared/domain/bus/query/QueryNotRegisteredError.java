package com.inditex.pricing.shared.domain.bus.query;

public final class QueryNotRegisteredError extends Exception {

  public QueryNotRegisteredError(Class<? extends Query> query) {
    super(String.format("La query <%s> no esta asociada a ningun handler", query.toString()));
  }
}
