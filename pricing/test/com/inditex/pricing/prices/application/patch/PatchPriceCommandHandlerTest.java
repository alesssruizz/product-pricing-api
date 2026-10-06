package com.inditex.pricing.prices.application.patch;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PatchPriceCommandHandler")
class PatchPriceCommandHandlerTest {

  @Mock private PricePatcher patcher;

  @InjectMocks private PatchPriceCommandHandler handler;

  @Test
  @DisplayName("Delegates the command to PricePatcher")
  void delegatesToPatcher() {
    PatchPriceCommand command =
        new PatchPriceCommand(
            "00000000-0000-0000-0000-000000000001",
            null,
            null,
            null,
            null,
            null,
            null,
            new BigDecimal("40.00"),
            null);

    handler.handle(command);

    verify(patcher).patch(command);
  }
}
