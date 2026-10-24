package com.gft.price.infrastructure.adapter.out.persistence.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gft.price.infrastructure.adapter.out.persistence.entity.PriceEntity;

public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {

    @Query("""
            SELECT p FROM PriceEntity p
            WHERE p.brandId = :brandId
            AND p.productId = :productId
            AND :applicationDate BETWEEN p.startDate AND p.endDate
            ORDER BY p.priority DESC
            LIMIT 1
            """)
    Optional<PriceEntity> findTopByBrandAndProductAndDate(
            @Param("brandId") Integer brandId,
            @Param("productId") Integer productId,
            @Param("applicationDate") LocalDateTime applicationDate);

}
