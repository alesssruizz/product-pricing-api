package com.inditex.pricing.shared.infrastructure.spring;

import java.util.Map;

import com.inditex.pricing.shared.domain.bus.command.Command;
import com.inditex.pricing.shared.domain.bus.command.CommandBus;
import com.inditex.pricing.shared.domain.bus.query.Query;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;
import com.inditex.pricing.shared.domain.exception.DomainError;

import org.springframework.http.HttpStatus;

public abstract class ApiController {

  private final QueryBus queryBus;

  private final CommandBus commandBus;

  protected ApiController(QueryBus queryBus, CommandBus commandBus) {
    this.queryBus = queryBus;
    this.commandBus = commandBus;
  }

  protected <R> R ask(Query query) {
    return queryBus.ask(query);
  }

  protected void dispatch(Command command) {
    commandBus.dispatch(command);
  }

  public Map<Class<? extends DomainError>, HttpStatus> errorMapping() {
    return Map.of();
  }
}
