package com.inditex.pricing.shared.infrastructure.bus.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.query.DuplicateQueryHandlerError;
import com.inditex.pricing.shared.domain.bus.query.Query;
import com.inditex.pricing.shared.domain.bus.query.QueryHandler;
import com.inditex.pricing.shared.domain.bus.query.QueryNotRegisteredError;

import org.springframework.core.ResolvableType;

@Service
@SuppressWarnings("rawtypes")
public final class QueryHandlersInformation {

  private final Map<Class<? extends Query>, QueryHandler> indexedQueryHandlers;

  public QueryHandlersInformation(List<QueryHandler> queryHandlers) {
    this.indexedQueryHandlers = formatHandlers(queryHandlers);
  }

  public QueryHandler search(Class<? extends Query> queryClass) throws QueryNotRegisteredError {
    QueryHandler queryHandler = indexedQueryHandlers.get(queryClass);

    if (null == queryHandler) {
      throw new QueryNotRegisteredError(queryClass);
    }

    return queryHandler;
  }

  private Map<Class<? extends Query>, QueryHandler> formatHandlers(
      List<QueryHandler> queryHandlers) {
    Map<Class<? extends Query>, QueryHandler> handlers = new HashMap<>();

    for (QueryHandler handler : queryHandlers) {
      Class<? extends Query> queryClass = resolveQueryType(handler);

      if (handlers.containsKey(queryClass)) {
        throw new DuplicateQueryHandlerError(queryClass);
      }

      handlers.put(queryClass, handler);
    }

    return handlers;
  }

  @SuppressWarnings("unchecked")
  private Class<? extends Query> resolveQueryType(QueryHandler handler) {
    Class<? extends Query> queryClass =
        (Class<? extends Query>)
            ResolvableType.forClass(handler.getClass()).as(QueryHandler.class).resolveGeneric(0);

    if (null == queryClass) {
      throw new IllegalStateException(
          String.format("Cannot resolve Query generic type for handler <%s>", handler.getClass()));
    }

    return queryClass;
  }
}
