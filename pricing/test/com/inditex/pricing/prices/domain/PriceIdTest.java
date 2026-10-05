package com.inditex.pricing.prices.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import com.inditex.pricing.shared.domain.InvalidUUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("PriceId")
class PriceIdTest {

  private static final String ID = "00000000-0000-0000-0000-000000000001";

  @Test
  void acceptsACanonicalUuid() {
    assertThat(new PriceId(ID).value()).isEqualTo(ID);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "abc", "1-1-1-1-1", "00000000-0000-0000-0000-00000000000g"})
  void rejectsNullBlankOrMalformedValues(String value) {
    assertThatThrownBy(() -> new PriceId(value))
        .isInstanceOfSatisfying(
            InvalidUUID.class, error -> assertThat(error.errorCode()).isEqualTo("invalid_uuid"));
  }

  @Test
  void normalisesUppercaseToLowercase() {
    assertThat(new PriceId(ID.replace('0', 'A').replace('1', 'B')).value())
        .isEqualTo(ID.replace('0', 'a').replace('1', 'b'));
  }

  @Test
  void roundTripsToUuid() {
    var uuid = UUID.fromString(ID);

    assertThat(new PriceId(ID).toUuid()).isEqualTo(uuid);
    assertThat(new PriceId(uuid.toString()).value()).isEqualTo(ID);
  }

  @Test
  void isEqualWhenTheUuidIsTheSame() {
    assertThat(new PriceId(ID)).isEqualTo(new PriceId(ID)).hasSameHashCodeAs(new PriceId(ID));
    assertThat(new PriceId(ID)).isNotEqualTo(new PriceId("00000000-0000-0000-0000-000000000002"));
  }
}
