package com.inditex.pricing.shared.infrastructure.bus.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inditex.pricing.prices.application.delete.DeletePriceCommand;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.bus.command.CommandHandler;
import com.inditex.pricing.shared.domain.bus.command.CommandHandlerExecutionError;
import com.inditex.pricing.shared.domain.bus.command.CommandNotRegisteredError;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("InMemoryCommandBus")
@SuppressWarnings({"rawtypes", "unchecked"})
class InMemoryCommandBusTest {

  @Mock private CommandHandlersInformation information;

  @Mock private CommandHandler handler;

  private InMemoryCommandBus bus;

  @BeforeEach
  void setUp() {
    bus = new InMemoryCommandBus(information);
  }

  @Test
  @DisplayName("Delegates the command to the registered handler")
  void delegatesToHandler() throws CommandNotRegisteredError {
    DeletePriceCommand command = new DeletePriceCommand("3f1c2b4e-8a6d-4c1e-9f2a-7b5d0e9c1a23");
    when(information.search(DeletePriceCommand.class)).thenReturn(handler);

    bus.dispatch(command);

    verify(handler).handle(command);
  }

  @Test
  @DisplayName("Wraps a domain error thrown by the handler")
  void wrapsDomainError() throws CommandNotRegisteredError {
    PriceNotFoundException cause =
        new PriceNotFoundException(new PriceId("3f1c2b4e-8a6d-4c1e-9f2a-7b5d0e9c1a23"));
    when(information.search(DeletePriceCommand.class)).thenReturn(handler);
    doThrow(cause).when(handler).handle(any());

    assertThatThrownBy(
            () -> bus.dispatch(new DeletePriceCommand("3f1c2b4e-8a6d-4c1e-9f2a-7b5d0e9c1a23")))
        .isInstanceOf(CommandHandlerExecutionError.class)
        .hasCause(cause);
  }

  @Test
  @DisplayName("Wraps a runtime error thrown by the handler")
  void wrapsRuntimeError() throws CommandNotRegisteredError {
    RuntimeException cause = new RuntimeException("boom");
    when(information.search(DeletePriceCommand.class)).thenReturn(handler);
    doThrow(cause).when(handler).handle(any());

    assertThatThrownBy(
            () -> bus.dispatch(new DeletePriceCommand("3f1c2b4e-8a6d-4c1e-9f2a-7b5d0e9c1a23")))
        .isInstanceOf(CommandHandlerExecutionError.class)
        .hasCause(cause);
  }

  @Test
  @DisplayName("Wraps the not registered error when no handler exists")
  void wrapsNotRegistered() throws CommandNotRegisteredError {
    DeletePriceCommand command = new DeletePriceCommand("3f1c2b4e-8a6d-4c1e-9f2a-7b5d0e9c1a23");
    CommandNotRegisteredError cause = new CommandNotRegisteredError(DeletePriceCommand.class);
    when(information.search(DeletePriceCommand.class)).thenThrow(cause);

    assertThatThrownBy(() -> bus.dispatch(command))
        .isInstanceOf(CommandHandlerExecutionError.class)
        .hasCause(cause);
  }
}
