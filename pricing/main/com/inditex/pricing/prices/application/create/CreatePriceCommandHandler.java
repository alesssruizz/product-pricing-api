package com.inditex.pricing.prices.application.create;

import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.PriceAmount;
import com.inditex.pricing.prices.domain.PriceCurrency;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceList;
import com.inditex.pricing.prices.domain.PricePriority;
import com.inditex.pricing.prices.domain.ProductId;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.command.CommandHandler;

@Service
public class CreatePriceCommandHandler implements CommandHandler<CreatePriceCommand> {

  private final PriceCreator creator;

  public CreatePriceCommandHandler(PriceCreator creator) {
    this.creator = creator;
  }

  @Override
  public void handle(CreatePriceCommand command) {

    final var priceId = new PriceId(command.id());
    final var brandId = new BrandId(command.brandId());
    final var productId = new ProductId(command.productId());
    final var priceList = new PriceList(command.priceList());
    final var priority = new PricePriority(command.priority());
    final String startDate = command.startDate();
    final String endDate = command.endDate();
    final var priceAmount = new PriceAmount(command.price());
    final var priceCurrency = new PriceCurrency(command.currency());

    creator.create(
        priceId,
        brandId,
        productId,
        priceList,
        priority,
        startDate,
        endDate,
        priceAmount,
        priceCurrency);
  }
}
