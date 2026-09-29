package com.inditex.pricing.shared.infraestructure.spring;

import java.util.HashMap;

import org.springframework.http.HttpStatus;

import com.inditex.pricing.shared.domain.DomainError;
import com.inditex.pricing.shared.domain.bus.query.Query;
import com.inditex.pricing.shared.domain.bus.query.QueryBus;

public abstract class ApiController {

    private final QueryBus queryBus;

    public ApiController(QueryBus queryBus) {
        this.queryBus = queryBus;
    }

    protected <R> R ask(Query query) {
        return queryBus.ask(query);
    }

    public abstract HashMap<Class<? extends DomainError>, HttpStatus> errorMapping();
}
