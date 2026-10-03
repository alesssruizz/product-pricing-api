package com.inditex.pricing.shared.infrastructure.bus.query;

import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.query.Query;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;
import com.inditex.pricing.shared.domain.bus.query.QueryHandler;
import com.inditex.pricing.shared.domain.bus.query.QueryHandlerExecutionError;

@Service
public final class InMemoryQueryBus implements QueryBus {

  private final QueryHandlersInformation information;

  public InMemoryQueryBus(QueryHandlersInformation information) {
    this.information = information;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public <R> R ask(Query query) throws QueryHandlerExecutionError {
    try {
      QueryHandler handler = information.search(query.getClass());

      return (R) handler.handle(query);
    } catch (Throwable error) {
      throw new QueryHandlerExecutionError(error);
    }
  }
}
