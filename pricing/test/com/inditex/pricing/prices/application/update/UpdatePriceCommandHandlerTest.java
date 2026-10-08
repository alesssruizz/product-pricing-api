package com.inditex.pricing.prices.application.update;

import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.PriceAmount;
import com.inditex.pricing.prices.domain.PriceCurrency;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceList;
import com.inditex.pricing.prices.domain.PricePriority;
import com.inditex.pricing.prices.domain.ProductId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdatePriceCommandHandler")
class UpdatePriceCommandHandlerTest {

  @Mock private PriceUpdater updater;

  @InjectMocks private UpdatePriceCommandHandler handler;

  @Test
  @DisplayName("Converts the command to value objects and delegates to PriceUpdater")
  void delegatesToUpdater() {
    UpdatePriceCommand command =
        new UpdatePriceCommand(
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

    verify(updater)
        .update(
            new PriceId(command.id()),
            new BrandId(command.brandId()),
            new ProductId(command.productId()),
            new PriceList(command.priceList()),
            new PricePriority(command.priority()),
            command.startDate(),
            command.endDate(),
            new PriceAmount(command.price()),
            new PriceCurrency(command.currency()));
  }
}
