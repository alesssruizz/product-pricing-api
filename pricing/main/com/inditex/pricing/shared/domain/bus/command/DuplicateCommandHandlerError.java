package com.inditex.pricing.shared.domain.bus.command;

public final class DuplicateCommandHandlerError extends RuntimeException {

  public DuplicateCommandHandlerError(Class<? extends Command> command) {
    super(String.format("Duplicate CommandHandler registered for command <%s>", command));
  }
}
