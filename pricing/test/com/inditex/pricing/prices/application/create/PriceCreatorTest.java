package com.inditex.pricing.prices.application.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import com.inditex.pricing.prices.application.PriceResponse;
import com.inditex.pricing.prices.domain.Price;
import com.inditex.pricing.prices.domain.PriceIntegrityChecker;
import com.inditex.pricing.prices.domain.PriceRepository;
import com.inditex.pricing.prices.domain.exceptions.PriceAlreadyExists;
import com.inditex.pricing.prices.domain.exceptions.PriceIdAlreadyExists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("PriceCreator")
class PriceCreatorTest {

  private static final String ID = "00000000-0000-0000-0000-000000000009";

  @Mock private PriceRepository repository;

  @Mock private PriceIntegrityChecker integrityChecker;

  private PriceCreator creator;

  @BeforeEach
  void setUp() {
    creator = new PriceCreator(repository, integrityChecker);
  }

  private static CreatePriceCommand command() {
    return new CreatePriceCommand(
        ID,
        1L,
        35455L,
        1,
        0,
        "2020-06-14T00:00:00",
        "2020-12-31T23:59:59",
        new BigDecimal("35.50"),
        "EUR");
  }

  @Nested
  @DisplayName("when the id is already taken")
  class WhenIdExists {

    @Test
    void throwsPriceIdAlreadyExistsWithoutCheckingIntegrityNorSaving() {
      when(repository.existsById(any())).thenReturn(true);

      assertThatThrownBy(() -> creator.create(command()))
          .isInstanceOfSatisfying(
              PriceIdAlreadyExists.class,
              error -> assertThat(error.errorCode()).isEqualTo("price_id_already_exists"));
      verify(integrityChecker, never()).ensureCanBeSaved(any());
      verify(repository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("when the id is free")
  class WhenIdIsFree {

    @Test
    void savesAndReturnsTheCreatedPrice() {
      when(repository.existsById(any())).thenReturn(false);
      when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

      PriceResponse response = creator.create(command());

      assertThat(response.id()).isEqualTo(ID);
      verify(integrityChecker).ensureCanBeSaved(any(Price.class));
      verify(repository).save(any(Price.class));
    }

    @Test
    void doesNotSaveWhenTheIntegrityCheckFails() {
      when(repository.existsById(any())).thenReturn(false);
      doThrow(new PriceAlreadyExists()).when(integrityChecker).ensureCanBeSaved(any());

      assertThatThrownBy(() -> creator.create(command())).isInstanceOf(PriceAlreadyExists.class);
      verify(repository, never()).save(any());
    }
  }
}
