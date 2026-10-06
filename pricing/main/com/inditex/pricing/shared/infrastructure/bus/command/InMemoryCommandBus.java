package com.inditex.pricing.shared.infrastructure.bus.command;

import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.command.Command;
import com.inditex.pricing.shared.domain.bus.command.CommandBus;
import com.inditex.pricing.shared.domain.bus.command.CommandHandler;
import com.inditex.pricing.shared.domain.bus.command.CommandHandlerExecutionError;

@Service
public final class InMemoryCommandBus implements CommandBus {

  private final CommandHandlersInformation information;

  public InMemoryCommandBus(CommandHandlersInformation information) {
    this.information = information;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void dispatch(Command command) {
    try {
      CommandHandler handler = information.search(command.getClass());

      handler.handle(command);
    } catch (Exception error) {
      throw new CommandHandlerExecutionError(error);
    }
  }
}
