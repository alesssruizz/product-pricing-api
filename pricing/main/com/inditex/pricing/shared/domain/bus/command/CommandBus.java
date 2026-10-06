package com.inditex.pricing.shared.domain.bus.command;

public interface CommandBus {
  void dispatch(Command command);
}
