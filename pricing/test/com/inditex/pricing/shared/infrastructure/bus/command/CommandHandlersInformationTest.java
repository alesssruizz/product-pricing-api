package com.inditex.pricing.shared.infrastructure.bus.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import com.inditex.pricing.shared.domain.bus.command.Command;
import com.inditex.pricing.shared.domain.bus.command.CommandHandler;
import com.inditex.pricing.shared.domain.bus.command.CommandNotRegisteredError;
import com.inditex.pricing.shared.domain.bus.command.DuplicateCommandHandlerError;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CommandHandlersInformation")
class CommandHandlersInformationTest {

  record FirstCommand() implements Command {}

  record SecondCommand() implements Command {}

  record UnknownCommand() implements Command {}

  static class FirstCommandHandler implements CommandHandler<FirstCommand> {
    @Override
    public void handle(FirstCommand command) {
      return;
    }
  }

  static class SecondCommandHandler implements CommandHandler<SecondCommand> {
    @Override
    public void handle(SecondCommand command) {
      return;
    }
  }

  static class DuplicatedFirstCommandHandler implements CommandHandler<FirstCommand> {
    @Override
    public void handle(FirstCommand command) {
      return;
    }
  }

  @Test
  @DisplayName("Resolves each handler by its command type")
  void resolvesHandlersByCommandType() throws CommandNotRegisteredError {
    FirstCommandHandler first = new FirstCommandHandler();
    SecondCommandHandler second = new SecondCommandHandler();
    CommandHandlersInformation information = new CommandHandlersInformation(List.of(first, second));

    assertThat(information.search(FirstCommand.class)).isSameAs(first);
    assertThat(information.search(SecondCommand.class)).isSameAs(second);
  }

  @Test
  @DisplayName("Throws not registered for a command without handler")
  void throwsNotRegisteredForUnknownCommand() {
    CommandHandlersInformation information =
        new CommandHandlersInformation(List.of(new FirstCommandHandler()));

    assertThatThrownBy(() -> information.search(UnknownCommand.class))
        .isInstanceOf(CommandNotRegisteredError.class);
  }

  @Test
  @DisplayName("Rejects two handlers for the same command type")
  void rejectsDuplicateHandlers() {
    List<CommandHandler> handlers =
        List.of(new FirstCommandHandler(), new DuplicatedFirstCommandHandler());
    assertThatThrownBy(() -> new CommandHandlersInformation(handlers))
        .isInstanceOf(DuplicateCommandHandlerError.class);
  }
}
