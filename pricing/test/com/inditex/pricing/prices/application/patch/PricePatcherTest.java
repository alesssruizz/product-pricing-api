package com.inditex.pricing.prices.application.patch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceAmount;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceIntegrityChecker;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.event.PriceUpdatedDomainEvent;
import com.inditex.pricing.prices.domain.exceptions.InvalidPriceDateRange;
import com.inditex.pricing.prices.domain.exceptions.PriceAlreadyExists;
import com.inditex.pricing.prices.domain.exceptions.PriceNotFoundException;
import com.inditex.pricing.shared.domain.bus.event.DomainEvent;
import com.inditex.pricing.shared.domain.bus.event.EventBus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PricePatcher")
@SuppressWarnings("unchecked")
class PricePatcherTest {

  private static final String ID = "00000000-0000-0000-0000-000000000001";

  @Mock private PriceRepository repository;

  @Mock private PriceIntegrityChecker integrityChecker;

  @Mock private EventBus eventBus;

  private PricePatcher patcher;

  @BeforeEach
  void setUp() {
    patcher = new PricePatcher(repository, integrityChecker, eventBus);
  }

  private static Price existing() {
    return Price.create(
        ID,
        1L,
        "2020-06-14T00:00:00",
        "2020-12-31T23:59:59",
        1,
        35455L,
        0,
        new BigDecimal("35.50"),
        "EUR");
  }

  private void patch(PriceAmount price, PriceDate startDate, PriceDate endDate) {
    patcher.patch(new PriceId(ID), null, null, null, null, startDate, endDate, price, null);
  }

  @Test
  @DisplayName("Keeps the stored values for fields that were not sent")
  void mergesOnlyTheSentFields() {
    when(repository.findById(any())).thenReturn(Optional.of(existing()));
    ArgumentCaptor<Price> saved = ArgumentCaptor.forClass(Price.class);

    patch(new PriceAmount(new BigDecimal("40.00")), null, null);

    verify(repository).update(saved.capture());
    verify(repository, never()).create(any());
    Price price = saved.getValue();
    assertThat(price.id().value()).isEqualTo(ID);
    assertThat(price.priceAmount().value()).isEqualByComparingTo("40.00");
    assertThat(price.priceList().value()).isEqualTo(1);
    assertThat(price.currency().value()).isEqualTo("EUR");
    assertThat(price.startDate().value().toString()).isEqualTo("2020-06-14T00:00");
    ArgumentCaptor<List<DomainEvent>> publishedEvents = ArgumentCaptor.forClass(List.class);
    verify(eventBus).publish(publishedEvents.capture());
    assertThat(publishedEvents.getValue())
        .singleElement()
        .isInstanceOf(PriceUpdatedDomainEvent.class);
  }

  @Test
  @DisplayName("Validates the merged final state and does not save when it is invalid")
  void validatesTheMergedFinalState() {
    when(repository.findById(any())).thenReturn(Optional.of(existing()));

    assertThatThrownBy(() -> patch(null, null, new PriceDate("2020-06-13T00:00:00")))
        .isInstanceOf(InvalidPriceDateRange.class);
    verify(repository, never()).update(any());
    verify(eventBus, never()).publish(any());
  }

  @Test
  @DisplayName("Throws not found when the id does not exist")
  void throwsNotFoundWhenMissing() {
    when(repository.findById(any())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> patch(new PriceAmount(new BigDecimal("40.00")), null, null))
        .isInstanceOf(PriceNotFoundException.class);
    verify(integrityChecker, never()).ensureCanBeSaved(any());
    verify(repository, never()).update(any());
    verify(eventBus, never()).publish(any());
  }

  @Test
  @DisplayName("Does not save when the integrity check reports a conflict")
  void doesNotSaveOnConflict() {
    when(repository.findById(any())).thenReturn(Optional.of(existing()));
    doThrow(new PriceAlreadyExists()).when(integrityChecker).ensureCanBeSaved(any());

    assertThatThrownBy(() -> patch(new PriceAmount(new BigDecimal("40.00")), null, null))
        .isInstanceOf(PriceAlreadyExists.class);
    verify(repository, never()).update(any());
    verify(eventBus, never()).publish(any());
  }
}
