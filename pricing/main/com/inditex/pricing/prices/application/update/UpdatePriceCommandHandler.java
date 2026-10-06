package com.inditex.pricing.prices.application.update;

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
    updater.update(command);
  }
}
