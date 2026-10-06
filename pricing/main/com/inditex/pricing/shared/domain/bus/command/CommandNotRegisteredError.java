package com.inditex.pricing.shared.domain.bus.command;

public final class CommandNotRegisteredError extends Exception {

  public CommandNotRegisteredError(Class<? extends Command> command) {
    super(String.format("El comando <%s> no esta asociado a ningun handler", command.toString()));
  }
}
