package com.inditex.pricing.prices.domain;

import com.inditex.pricing.prices.domain.policy.PriceConflictPolicy;
import com.inditex.pricing.prices.domain.policy.PriceReferencesPolicy;
import com.inditex.pricing.shared.domain.Service;

@Service
public class PriceIntegrityChecker {

  private final PriceReferencesPolicy referencesPolicy;

  private final PriceConflictPolicy conflictPolicy;

  public PriceIntegrityChecker(
      PriceReferencesPolicy referencesPolicy, PriceConflictPolicy conflictPolicy) {
    this.referencesPolicy = referencesPolicy;
    this.conflictPolicy = conflictPolicy;
  }

  public void ensureCanBeSaved(Price price) {
    referencesPolicy.ensureReferencesExist(price);
    conflictPolicy.ensureNoConflict(price);
  }
}
