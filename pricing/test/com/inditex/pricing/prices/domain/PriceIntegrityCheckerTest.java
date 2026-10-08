package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoInteractions;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.exception.InvalidPriceReference;
import com.inditex.pricing.prices.domain.exception.PriceAlreadyExists;
import com.inditex.pricing.prices.domain.policy.PriceConflictPolicy;
import com.inditex.pricing.prices.domain.policy.PriceReferencesPolicy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PriceIntegrityChecker")
class PriceIntegrityCheckerTest {

  @Mock private PriceReferencesPolicy referencesPolicy;

  @Mock private PriceConflictPolicy conflictPolicy;

  private PriceIntegrityChecker checker;

  @BeforeEach
  void setUp() {
    checker = new PriceIntegrityChecker(referencesPolicy, conflictPolicy);
  }

  private static Price price() {
    return Price.create(
        new PriceId("00000000-0000-0000-0000-000000000001"),
        new BrandId(1L),
        new PriceDate("2020-06-14T00:00:00"),
        new PriceDate("2020-12-31T23:59:59"),
        new PriceList(1),
        new ProductId(35455L),
        new PricePriority(0),
        new PriceAmount(BigDecimal.TEN),
        new PriceCurrency("EUR"));
  }

  @Test
  void checksReferencesBeforeConflicts() {
    var price = price();

    checker.ensureCanBeSaved(price);

    var order = inOrder(referencesPolicy, conflictPolicy);
    order.verify(referencesPolicy).ensureReferencesExist(price);
    order.verify(conflictPolicy).ensureNoConflict(price);
  }

  @Test
  void doesNotCheckConflictsWhenReferencesAreInvalid() {
    doThrow(new InvalidPriceReference("brand", 1L))
        .when(referencesPolicy)
        .ensureReferencesExist(any());

    assertThatThrownBy(() -> checker.ensureCanBeSaved(price()))
        .isInstanceOf(InvalidPriceReference.class);
    verifyNoInteractions(conflictPolicy);
  }

  @Test
  void propagatesAConflict() {
    doThrow(new PriceAlreadyExists()).when(conflictPolicy).ensureNoConflict(any());

    assertThatThrownBy(() -> checker.ensureCanBeSaved(price()))
        .isInstanceOf(PriceAlreadyExists.class);
  }
}
