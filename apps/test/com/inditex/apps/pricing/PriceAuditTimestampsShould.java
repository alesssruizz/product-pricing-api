package com.inditex.apps.pricing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.UUID;

import com.inditex.pricing.prices.infrastructure.persistence.jpa.PriceJpaEntity;
import com.inditex.pricing.prices.infrastructure.persistence.jpa.PriceJpaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PriceAuditTimestampsShould extends ProductPricingApiApplicationTests {

  private static final UUID SEED_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

  private static final UUID NEW_ID = UUID.fromString("00000000-0000-0000-0000-000000000009");

  private static final Instant SEED_CREATED_AT = Instant.parse("2020-01-01T00:00:00Z");

  private static final Instant SEED_UPDATED_AT = Instant.parse("2020-01-02T00:00:00Z");

  private static final String SEED_ENDPOINT = "/api/v1/prices/" + SEED_ID;

  private static final String CREATE_BODY =
      """
      {
          "id": "00000000-0000-0000-0000-000000000009",
          "brandId": 1,
          "productId": 35455,
          "priceList": 9,
          "priority": 5,
          "startDate": "2021-01-01T00:00:00",
          "endDate": "2021-01-31T23:59:59",
          "price": 12.30,
          "currency": "EUR"
      }
      """;

  private static final String CHANGED_BODY =
      """
      {
          "brandId": 1,
          "productId": 35455,
          "priceList": 1,
          "priority": 0,
          "startDate": "2020-06-14T00:00:00",
          "endDate": "2020-12-31T23:59:59",
          "price": 40.00,
          "currency": "EUR"
      }
      """;

  private static final String SEED_BODY =
      """
      {
          "brandId": 1,
          "productId": 35455,
          "priceList": 1,
          "priority": 0,
          "startDate": "2020-06-14T00:00:00",
          "endDate": "2020-12-31T23:59:59",
          "price": 35.50,
          "currency": "EUR"
      }
      """;

  @Autowired private PriceJpaRepository repository;

  @PersistenceContext private EntityManager entityManager;

  private PriceJpaEntity reload(UUID id) {
    entityManager.flush();
    entityManager.clear();
    return repository.findById(id).orElseThrow();
  }

  private void putSeed(String body) throws Exception {
    perform(put(SEED_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isNoContent());
  }

  private void patchSeed(String body) throws Exception {
    perform(patch(SEED_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isNoContent());
  }

  @Nested
  class WhenCreating {

    @Test
    @DisplayName("Sets createdAt and updatedAt, with updatedAt not before createdAt")
    void setsBothTimestamps() throws Exception {
      postBody("/api/v1/prices", CREATE_BODY).andExpect(status().isCreated());

      var entity = reload(NEW_ID);

      assertThat(entity.getCreatedAt()).isNotNull().isAfter(SEED_UPDATED_AT);
      assertThat(entity.getUpdatedAt()).isNotNull().isAfterOrEqualTo(entity.getCreatedAt());
    }
  }

  @Nested
  class WhenReplacing {

    @Test
    @DisplayName("Keeps createdAt and refreshes updatedAt")
    void keepsCreatedAtAndRefreshesUpdatedAt() throws Exception {
      putSeed(CHANGED_BODY);

      var entity = reload(SEED_ID);

      assertThat(entity.getCreatedAt()).isEqualTo(SEED_CREATED_AT);
      assertThat(entity.getUpdatedAt()).isAfter(SEED_UPDATED_AT);
    }
  }

  @Nested
  class WhenPatching {

    @Test
    @DisplayName("Keeps createdAt and refreshes updatedAt")
    void keepsCreatedAtAndRefreshesUpdatedAt() throws Exception {
      patchSeed("{\"price\": 41.00}");

      var entity = reload(SEED_ID);

      assertThat(entity.getCreatedAt()).isEqualTo(SEED_CREATED_AT);
      assertThat(entity.getUpdatedAt()).isAfter(SEED_UPDATED_AT);
    }
  }

  @Nested
  class WhenReplacingWithSameValues {

    @Test
    @DisplayName("Keeps createdAt and still refreshes updatedAt (accepted no-op bump)")
    void stillRefreshesUpdatedAt() throws Exception {
      putSeed(SEED_BODY);

      var entity = reload(SEED_ID);

      assertThat(entity.getCreatedAt()).isEqualTo(SEED_CREATED_AT);
      assertThat(entity.getUpdatedAt()).isAfter(SEED_UPDATED_AT);
    }
  }
}
