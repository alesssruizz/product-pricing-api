package com.inditex.pricing.prices.application.create;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreatePriceCommandHandler")
class CreatePriceCommandHandlerTest {

  @Mock private PriceCreator creator;

  @InjectMocks private CreatePriceCommandHandler handler;

  @Test
  @DisplayName("Delegates the command to PriceCreator")
  void delegatesToCreator() {
    CreatePriceCommand command =
        new CreatePriceCommand(
            "00000000-0000-0000-0000-000000000001",
            1L,
            35455L,
            1,
            0,
            "2020-06-14T00:00:00",
            "2020-12-31T23:59:59",
            new BigDecimal("35.50"),
            "EUR");

    handler.handle(command);

    verify(creator).create(command);
  }
}
