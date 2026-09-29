package com.inditex.pricing.prices.infrastructure.persistence.jpa;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataPriceRepository extends JpaRepository<PriceJpaEntity, Long> {

	// Utilizo mejor query ya que pienso que es mucho mas declarativo que el metodo verboso de Jpa
    @Query(
        value = """
        SELECT p
        FROM PriceJpaEntity p
        WHERE p.brandId = :brandId
        	AND p.productId = :productId
        	AND :applicationDate BETWEEN p.startDate AND p.endDate
        ORDER BY p.priority DESC, p.startDate DESC
        LIMIT 1
        """
    )
    Optional<PriceJpaEntity> findApplicablePrice(
        Long brandId,
        Long productId,
        LocalDateTime applicationDate
    );

    /*
     * Equivalente a la query anterior, pero usando el metodo de Spring Data JPA derivado del nombre del metodo.
     *
    Optional<
        PriceJpaEntity
    > findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDescStartDateDesc(
        Long brandId,
        Long productId,
        LocalDateTime applicationDateAsStartDateUpperBound,
        LocalDateTime applicationDateAsEndDateLowerBound
    );
    */
}
