package com.inditex.pricing.prices.application.patch;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.PriceAmount;
import com.inditex.pricing.prices.domain.PriceId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PatchPriceCommandHandler")
class PatchPriceCommandHandlerTest {

  private static final String ID = "00000000-0000-0000-0000-000000000001";

  @Mock private PricePatcher patcher;

  @InjectMocks private PatchPriceCommandHandler handler;

  @Test
  @DisplayName("Converts only the sent fields to value objects and delegates to PricePatcher")
  void delegatesToPatcher() {
    PatchPriceCommand command =
        new PatchPriceCommand(
            ID, null, null, null, null, null, null, new BigDecimal("40.00"), null);

    handler.handle(command);

    verify(patcher)
        .patch(
            new PriceId(ID),
            null,
            null,
            null,
            null,
            null,
            null,
            new PriceAmount(new BigDecimal("40.00")),
            null);
  }
}
