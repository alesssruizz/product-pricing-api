package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import com.inditex.pricing.prices.domain.exception.InvalidPriceReference;
import com.inditex.pricing.prices.domain.exception.PriceAlreadyExists;
import com.inditex.pricing.prices.domain.policy.PriceConflictPolicy;
import com.inditex.pricing.prices.domain.policy.PricePolicy;
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

  @Mock private PricePolicy first;

  @Mock private PricePolicy second;

  private PriceIntegrityChecker checker;

  @BeforeEach
  void setUp() {
    when(first.order()).thenReturn(10);
    when(second.order()).thenReturn(20);
    checker = new PriceIntegrityChecker(List.of(second, first));
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
  @DisplayName("Runs every policy sorted by order, regardless of injection order")
  void runsPoliciesSortedByOrder() {
    var price = price();

    checker.ensureCanBeSaved(price);

    var order = inOrder(first, second);
    order.verify(first).ensure(price);
    order.verify(second).ensure(price);
  }

  @Test
  @DisplayName("Stops at the first policy that fails")
  void stopsAtTheFirstFailingPolicy() {
    doThrow(new InvalidPriceReference("brand", 1L)).when(first).ensure(any());

    assertThatThrownBy(() -> checker.ensureCanBeSaved(price()))
        .isInstanceOf(InvalidPriceReference.class);
    verify(second, never()).ensure(any());
  }

  @Test
  @DisplayName("Propagates the error of a later policy")
  void propagatesTheErrorOfALaterPolicy() {
    doThrow(new PriceAlreadyExists()).when(second).ensure(any());

    assertThatThrownBy(() -> checker.ensureCanBeSaved(price()))
        .isInstanceOf(PriceAlreadyExists.class);
  }

  @Test
  @DisplayName("Checks references before conflicts")
  void checksReferencesBeforeConflicts() {
    assertThat(new PriceReferencesPolicy(null).order())
        .isLessThan(new PriceConflictPolicy(null).order());
  }
}
