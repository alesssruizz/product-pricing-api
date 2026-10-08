package com.inditex.pricing.prices.domain;

import java.util.Comparator;
import java.util.List;

import com.inditex.pricing.prices.domain.policy.PricePolicy;
import com.inditex.pricing.shared.domain.Service;

@Service
public class PriceIntegrityChecker {

  private final List<PricePolicy> policies;

  public PriceIntegrityChecker(List<PricePolicy> policies) {
    this.policies = policies.stream().sorted(Comparator.comparingInt(PricePolicy::order)).toList();
  }

  public void ensureCanBeSaved(Price price) {
    policies.forEach(policy -> policy.ensure(price));
  }
}
