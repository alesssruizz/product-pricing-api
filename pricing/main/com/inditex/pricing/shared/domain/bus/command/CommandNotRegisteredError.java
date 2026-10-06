package com.inditex.pricing.shared.domain.bus.command;

public final class CommandNotRegisteredError extends Exception {

  public CommandNotRegisteredError(Class<? extends Command> command) {
    super(String.format("No handler is registered for command <%s>", command.toString()));
  }
}
