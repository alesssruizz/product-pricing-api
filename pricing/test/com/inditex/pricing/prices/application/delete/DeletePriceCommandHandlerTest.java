package com.inditex.pricing.prices.application.delete;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeletePriceCommandHandler")
class DeletePriceCommandHandlerTest {

  private static final String ID = "00000000-0000-0000-0000-000000000001";

  @Mock private PriceDeleter deleter;

  @InjectMocks private DeletePriceCommandHandler handler;

  @Test
  @DisplayName("Delegates the id to PriceDeleter")
  void delegatesToDeleter() {
    handler.handle(new DeletePriceCommand(ID));

    verify(deleter).delete(ID);
  }
}
