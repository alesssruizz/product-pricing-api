package com.inditex.pricing.prices.application.delete;

import com.inditex.pricing.shared.domain.bus.command.Command;

public record DeletePriceCommand(String id) implements Command {}
