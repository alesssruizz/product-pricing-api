package com.inditex.pricing.brands.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "brands")
public class BrandJpaEntity {

  @Id private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  protected BrandJpaEntity() {
    // Required by JPA
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
