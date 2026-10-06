package com.inditex.pricing.prices.application.patch;

import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.command.CommandHandler;

@Service
public class PatchPriceCommandHandler implements CommandHandler<PatchPriceCommand> {

  private final PricePatcher patcher;

  public PatchPriceCommandHandler(PricePatcher patcher) {
    this.patcher = patcher;
  }

  @Override
  public void handle(PatchPriceCommand command) {
    patcher.patch(command);
  }
}
