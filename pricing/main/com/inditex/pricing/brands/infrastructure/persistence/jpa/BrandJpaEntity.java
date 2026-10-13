package com.inditex.pricing.brands.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Immutable;

@Accessors(fluent = false)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Immutable
@Table(name = "brands")
public class BrandJpaEntity {

  @Id private Long id;

  @Column(nullable = false, length = 100)
  private String name;
}
