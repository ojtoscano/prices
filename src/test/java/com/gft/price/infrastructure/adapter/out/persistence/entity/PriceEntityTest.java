package com.gft.price.infrastructure.adapter.out.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceEntityTest {

    @Test
    @DisplayName("debe exponer los valores persistidos mediante sus getters")
    void shouldExposePersistedValues() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59, 59);
        BigDecimal amount = new BigDecimal("35.50");

        PriceEntity entity = new PriceEntity(1L, 1, 35455, 1, startDate, endDate, 0, amount, "EUR");

        assertEquals(1L, entity.getId());
        assertEquals(1, entity.getBrandId());
        assertEquals(35455, entity.getProductId());
        assertEquals(1, entity.getTariffId());
        assertEquals(startDate, entity.getStartDate());
        assertEquals(endDate, entity.getEndDate());
        assertEquals(0, entity.getPriority());
        assertEquals(amount, entity.getAmount());
        assertEquals("EUR", entity.getCurrencyCode());
    }

    @Test
    @DisplayName("debe permitir la creación mediante el constructor sin argumentos requerido por JPA")
    void shouldProvideNoArgsConstructorForJpa() {
        PriceEntity entity = new PriceEntity();

        assertEquals(null, entity.getId());
        assertEquals(null, entity.getAmount());
    }
}
