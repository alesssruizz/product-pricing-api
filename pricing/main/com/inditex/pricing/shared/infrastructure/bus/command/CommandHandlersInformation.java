package com.inditex.pricing.shared.infrastructure.bus.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.command.Command;
import com.inditex.pricing.shared.domain.bus.command.CommandHandler;
import com.inditex.pricing.shared.domain.bus.command.CommandNotRegisteredError;
import com.inditex.pricing.shared.domain.bus.command.DuplicateCommandHandlerError;

import org.springframework.core.ResolvableType;

@Service
@SuppressWarnings("rawtypes")
public final class CommandHandlersInformation {

  private final Map<Class<? extends Command>, CommandHandler> indexedCommandHandlers;

  public CommandHandlersInformation(List<CommandHandler> commandHandlers) {
    this.indexedCommandHandlers = formatHandlers(commandHandlers);
  }

  public CommandHandler search(Class<? extends Command> commandClass)
      throws CommandNotRegisteredError {
    CommandHandler commandHandler = indexedCommandHandlers.get(commandClass);

    if (null == commandHandler) {
      throw new CommandNotRegisteredError(commandClass);
    }

    return commandHandler;
  }

  private Map<Class<? extends Command>, CommandHandler> formatHandlers(
      List<CommandHandler> commandHandlers) {
    Map<Class<? extends Command>, CommandHandler> handlers = new HashMap<>();

    for (CommandHandler handler : commandHandlers) {
      Class<? extends Command> commandClass = resolveCommandType(handler);

      if (handlers.containsKey(commandClass)) {
        throw new DuplicateCommandHandlerError(commandClass);
      }

      handlers.put(commandClass, handler);
    }

    return handlers;
  }

  @SuppressWarnings("unchecked")
  private Class<? extends Command> resolveCommandType(CommandHandler handler) {
    Class<? extends Command> commandClass =
        (Class<? extends Command>)
            ResolvableType.forClass(handler.getClass()).as(CommandHandler.class).resolveGeneric(0);

    if (null == commandClass) {
      throw new IllegalStateException(
          String.format(
              "Cannot resolve Command generic type for handler <%s>", handler.getClass()));
    }

    return commandClass;
  }
}
