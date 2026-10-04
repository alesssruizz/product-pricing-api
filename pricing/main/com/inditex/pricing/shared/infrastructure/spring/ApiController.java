package com.inditex.pricing.shared.infrastructure.spring;

import java.util.Map;

import com.inditex.pricing.shared.domain.DomainError;
import com.inditex.pricing.shared.domain.bus.query.Query;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;

import org.springframework.http.HttpStatus;

public abstract class ApiController {

  private final QueryBus queryBus;

  public ApiController(QueryBus queryBus) {
    this.queryBus = queryBus;
  }

  protected <R> R ask(Query query) {
    return queryBus.ask(query);
  }

  public Map<Class<? extends DomainError>, HttpStatus> errorMapping() {
    return Map.of();
  }
}
