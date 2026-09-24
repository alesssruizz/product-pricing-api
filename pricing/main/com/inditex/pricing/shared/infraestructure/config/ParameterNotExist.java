package com.inditex.pricing.shared.infraestructure.config;

public final class ParameterNotExist extends Exception {

    public ParameterNotExist(String key) {
        super(String.format("The parameter <%s> does not exist in the environment file", key));
    }
}
