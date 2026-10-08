package com.inditex.pricing.prices.application.patch;

import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.PriceAmount;
import com.inditex.pricing.prices.domain.PriceCurrency;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceList;
import com.inditex.pricing.prices.domain.PricePriority;
import com.inditex.pricing.prices.domain.ProductId;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.Utils;
import com.inditex.pricing.shared.domain.bus.command.CommandHandler;

@Service
public class PatchPriceCommandHandler implements CommandHandler<PatchPriceCommand> {

  private final PricePatcher patcher;

  public PatchPriceCommandHandler(PricePatcher patcher) {
    this.patcher = patcher;
  }

  @Override
  public void handle(PatchPriceCommand command) {
    var id = new PriceId(command.id());
    var brandId = Utils.convertIfPresent(command.brandId(), BrandId::new);
    var productId = Utils.convertIfPresent(command.productId(), ProductId::new);
    var priceList = Utils.convertIfPresent(command.priceList(), PriceList::new);
    var priority = Utils.convertIfPresent(command.priority(), PricePriority::new);
    var startDate = Utils.convertIfPresent(command.startDate(), PriceDate::new);
    var endDate = Utils.convertIfPresent(command.endDate(), PriceDate::new);
    var price = Utils.convertIfPresent(command.price(), PriceAmount::new);
    var currency = Utils.convertIfPresent(command.currency(), PriceCurrency::new);

    patcher.patch(id, brandId, productId, priceList, priority, startDate, endDate, price, currency);
  }
}
