package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.inditex.pricing.prices.domain.exception.InvalidPriceReference;
import com.inditex.pricing.prices.domain.exception.PriceAlreadyExists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PriceIntegrityChecker")
class PriceIntegrityCheckerTest {

  @Mock private PriceRepository repository;

  @Mock private PriceReferences references;

  private PriceIntegrityChecker checker;

  @BeforeEach
  void setUp() {
    checker = new PriceIntegrityChecker(repository, references);
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
  void allowsAPriceWhenReferencesExistAndNoConflict() {
    when(references.brandExists(any())).thenReturn(true);
    when(references.productExists(any())).thenReturn(true);
    when(repository.existsConflict(any())).thenReturn(false);

    assertThatCode(() -> checker.ensureCanBeSaved(price())).doesNotThrowAnyException();
  }

  @Test
  void rejectsAnUnknownBrandBeforeCheckingConflicts() {
    when(references.brandExists(any())).thenReturn(false);

    assertThatThrownBy(() -> checker.ensureCanBeSaved(price()))
        .isInstanceOf(InvalidPriceReference.class);
    verify(repository, never()).existsConflict(any());
  }

  @Test
  void rejectsAnUnknownProductBeforeCheckingConflicts() {
    when(references.brandExists(any())).thenReturn(true);
    when(references.productExists(any())).thenReturn(false);

    assertThatThrownBy(() -> checker.ensureCanBeSaved(price()))
        .isInstanceOf(InvalidPriceReference.class);
    verify(repository, never()).existsConflict(any());
  }

  @Test
  void rejectsAConflictingPrice() {
    when(references.brandExists(any())).thenReturn(true);
    when(references.productExists(any())).thenReturn(true);
    when(repository.existsConflict(any())).thenReturn(true);

    assertThatThrownBy(() -> checker.ensureCanBeSaved(price()))
        .isInstanceOf(PriceAlreadyExists.class);
  }
}
