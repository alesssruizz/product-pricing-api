package com.inditex.pricing.prices.application.create;

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
    creator.create(command);
  }
}
