package com.gft.price.infrastructure.adapter.out.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:price-repository-test;DB_CLOSE_DELAY=-1")
class PriceJpaRepositoryTest {

    @Autowired
    private PriceJpaRepository priceJpaRepository;

    @Test
    @DisplayName("debe seleccionar la tarifa de mayor prioridad dentro del intervalo")
    void shouldSelectHighestPriorityPriceWithinDateRange() {
        var result = priceJpaRepository.findTopByBrandAndProductAndDate(
                1, 35455, LocalDateTime.of(2020, 6, 14, 16, 0));

        assertTrue(result.isPresent());
        assertEquals(2, result.get().getTariffId());
        assertEquals(new BigDecimal("25.45"), result.get().getAmount());
    }

    @Test
    @DisplayName("debe considerar inclusivos los límites del intervalo de vigencia")
    void shouldIncludeStartAndEndDateBoundaries() {
        var atStart = priceJpaRepository.findTopByBrandAndProductAndDate(
                1, 35455, LocalDateTime.of(2020, 6, 14, 0, 0));
        var atEnd = priceJpaRepository.findTopByBrandAndProductAndDate(
                1, 35455, LocalDateTime.of(2020, 12, 31, 23, 59, 59));

        assertTrue(atStart.isPresent());
        assertEquals(1, atStart.get().getTariffId());
        assertTrue(atEnd.isPresent());
        assertEquals(4, atEnd.get().getTariffId());
    }

    @Test
    @DisplayName("debe devolver vacío fuera de todos los intervalos o para otro producto")
    void shouldReturnEmptyWhenNoPriceMatches() {
        var outsideRange = priceJpaRepository.findTopByBrandAndProductAndDate(
                1, 35455, LocalDateTime.of(2021, 1, 1, 0, 0));
        var otherProduct = priceJpaRepository.findTopByBrandAndProductAndDate(
                1, 99999, LocalDateTime.of(2020, 6, 14, 16, 0));

        assertTrue(outsideRange.isEmpty());
        assertTrue(otherProduct.isEmpty());
    }
}
