package com.inditex.pricing.prices.application.update;

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
import com.inditex.pricing.prices.domain.event.PriceUpdatedDomainEvent;
import com.inditex.pricing.prices.domain.exception.PriceAlreadyExists;
import com.inditex.pricing.prices.domain.exception.PriceNotFoundException;
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
@DisplayName("PriceUpdater")
@SuppressWarnings("unchecked")
class PriceUpdaterTest {

  private static final String ID = "00000000-0000-0000-0000-000000000001";

  @Mock private PriceRepository repository;

  @Mock private PriceIntegrityChecker integrityChecker;

  @Mock private EventBus eventBus;

  private PriceUpdater updater;

  @BeforeEach
  void setUp() {
    updater = new PriceUpdater(repository, integrityChecker, eventBus);
  }

  private void update() {
    updater.update(
        new PriceId(ID),
        new BrandId(1L),
        new PriceDate("2020-06-14T00:00:00"),
        new PriceDate("2020-12-31T23:59:59"),
        new PriceList(2),
        new ProductId(35455L),
        new PricePriority(1),
        new PriceAmount(new BigDecimal("40.00")),
        new PriceCurrency("EUR"));
  }

  @Nested
  @DisplayName("when the id does not exist")
  class WhenIdIsMissing {

    @Test
    void throwsNotFoundWithoutCheckingIntegrityNorUpdating() {
      when(repository.existsById(any())).thenReturn(false);

      assertThatThrownBy(PriceUpdaterTest.this::update).isInstanceOf(PriceNotFoundException.class);
      verify(integrityChecker, never()).ensureCanBeSaved(any());
      verify(repository, never()).update(any());
      verify(eventBus, never()).publish(any());
    }
  }

  @Nested
  @DisplayName("when the id exists")
  class WhenIdExists {

    @Test
    void updatesThePriceWithTheGivenValues() {
      when(repository.existsById(any())).thenReturn(true);
      ArgumentCaptor<Price> saved = ArgumentCaptor.forClass(Price.class);

      update();

      verify(integrityChecker).ensureCanBeSaved(any(Price.class));
      verify(repository).update(saved.capture());
      verify(repository, never()).create(any());
      Price price = saved.getValue();
      assertThat(price.id().value()).isEqualTo(ID);
      assertThat(price.brandId().value()).isEqualTo(1L);
      assertThat(price.productId().value()).isEqualTo(35455L);
      assertThat(price.priceList().value()).isEqualTo(2);
      assertThat(price.priority().value()).isEqualTo(1);
      assertThat(price.startDate().value().toString()).isEqualTo("2020-06-14T00:00");
      assertThat(price.endDate().value().toString()).isEqualTo("2020-12-31T23:59:59");
      assertThat(price.priceAmount().value()).isEqualByComparingTo("40.00");
      assertThat(price.currency().value()).isEqualTo("EUR");
      ArgumentCaptor<List<DomainEvent>> publishedEvents = ArgumentCaptor.forClass(List.class);
      verify(eventBus).publish(publishedEvents.capture());
      assertThat(publishedEvents.getValue())
          .singleElement()
          .isInstanceOf(PriceUpdatedDomainEvent.class);
    }

    @Test
    void doesNotUpdateWhenTheIntegrityCheckFails() {
      when(repository.existsById(any())).thenReturn(true);
      doThrow(new PriceAlreadyExists()).when(integrityChecker).ensureCanBeSaved(any());

      assertThatThrownBy(PriceUpdaterTest.this::update).isInstanceOf(PriceAlreadyExists.class);
      verify(repository, never()).update(any());
      verify(eventBus, never()).publish(any());
    }
  }
}
