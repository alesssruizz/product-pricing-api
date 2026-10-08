package com.inditex.pricing.prices.application.delete;

import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.shared.domain.Service;
import com.inditex.pricing.shared.domain.bus.command.CommandHandler;

@Service
public class DeletePriceCommandHandler implements CommandHandler<DeletePriceCommand> {

  private final PriceDeleter deleter;

  public DeletePriceCommandHandler(PriceDeleter deleter) {
    this.deleter = deleter;
  }

  @Override
  public void handle(DeletePriceCommand command) {
    deleter.delete(new PriceId(command.id()));
  }
}
