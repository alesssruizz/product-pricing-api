package com.inditex.pricing.prices.application.update;

import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.PriceAmount;
import com.inditex.pricing.prices.domain.PriceCurrency;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceList;
import com.inditex.pricing.prices.domain.PricePriority;
import com.inditex.pricing.prices.domain.ProductId;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.command.CommandHandler;

@Service
public class UpdatePriceCommandHandler implements CommandHandler<UpdatePriceCommand> {

  private final PriceUpdater updater;

  public UpdatePriceCommandHandler(PriceUpdater updater) {
    this.updater = updater;
  }

  @Override
  public void handle(UpdatePriceCommand command) {
    var id = new PriceId(command.id());
    var brandId = new BrandId(command.brandId());
    var productId = new ProductId(command.productId());
    var priceList = new PriceList(command.priceList());
    var priority = new PricePriority(command.priority());
    var startDate = new PriceDate(command.startDate());
    var endDate = new PriceDate(command.endDate());
    var price = new PriceAmount(command.price());
    var currency = new PriceCurrency(command.currency());

    updater.update(
        id, brandId, startDate, endDate, priceList, productId, priority, price, currency);
  }
}
