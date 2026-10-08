package com.inditex.pricing.prices.application.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import com.inditex.pricing.prices.domain.BrandId;
import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceAmount;
import com.inditex.pricing.prices.domain.PriceCurrency;
import com.inditex.pricing.prices.domain.PriceDate;
import com.inditex.pricing.prices.domain.PriceId;
import com.inditex.pricing.prices.domain.PriceIntegrityChecker;
import com.inditex.pricing.prices.domain.PriceList;
import com.inditex.pricing.prices.domain.PricePriority;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.ProductId;
import com.inditex.pricing.prices.domain.event.PriceCreatedDomainEvent;
import com.inditex.pricing.prices.domain.exception.PriceAlreadyExists;
import com.inditex.pricing.prices.domain.exception.PriceIdAlreadyExists;
import com.inditex.pricing.shared.domain.bus.event.DomainEvent;
import com.inditex.pricing.shared.domain.bus.event.EventBus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PriceCreator")
@SuppressWarnings("unchecked")
class PriceCreatorTest {

  private static final String ID = "00000000-0000-0000-0000-000000000009";

  @Mock private PriceRepository repository;

  @Mock private PriceIntegrityChecker integrityChecker;

  @Mock private EventBus eventBus;

  private PriceCreator creator;

  @BeforeEach
  void setUp() {
    creator = new PriceCreator(repository, integrityChecker, eventBus);
  }

  private void create() {
    creator.create(
        new PriceId(ID),
        new BrandId(1L),
        new PriceDate("2020-06-14T00:00:00"),
        new PriceDate("2020-12-31T23:59:59"),
        new PriceList(1),
        new ProductId(35455L),
        new PricePriority(0),
        new PriceAmount(new BigDecimal("35.50")),
        new PriceCurrency("EUR"));
  }

  @Nested
  @DisplayName("when the id is already taken")
  class WhenIdExists {

    @Test
    void throwsPriceIdAlreadyExistsWithoutCheckingIntegrityNorSaving() {
      when(repository.existsById(any())).thenReturn(true);

      assertThatThrownBy(PriceCreatorTest.this::create)
          .isInstanceOfSatisfying(
              PriceIdAlreadyExists.class,
              error -> assertThat(error.errorCode()).isEqualTo("price_id_already_exists"));
      verify(integrityChecker, never()).ensureCanBeSaved(any());
      verify(repository, never()).create(any());
      verify(eventBus, never()).publish(any());
    }
  }

  @Nested
  @DisplayName("when the id is free")
  class WhenIdIsFree {

    @Test
    void createsThePrice() {
      when(repository.existsById(any())).thenReturn(false);
      ArgumentCaptor<Price> saved = ArgumentCaptor.forClass(Price.class);

      create();

      verify(integrityChecker).ensureCanBeSaved(any(Price.class));
      verify(repository).create(saved.capture());
      verify(repository, never()).update(any());
      ArgumentCaptor<List<DomainEvent>> publishedEvents = ArgumentCaptor.forClass(List.class);
      verify(eventBus).publish(publishedEvents.capture());
      assertThat(publishedEvents.getValue())
          .singleElement()
          .isInstanceOf(PriceCreatedDomainEvent.class);
      assertThat(saved.getValue().id().value()).isEqualTo(ID);
    }

    @Test
    void doesNotSaveWhenTheIntegrityCheckFails() {
      when(repository.existsById(any())).thenReturn(false);
      doThrow(new PriceAlreadyExists()).when(integrityChecker).ensureCanBeSaved(any());

      assertThatThrownBy(PriceCreatorTest.this::create).isInstanceOf(PriceAlreadyExists.class);
      verify(repository, never()).create(any());
      verify(eventBus, never()).publish(any());
    }
  }
}
