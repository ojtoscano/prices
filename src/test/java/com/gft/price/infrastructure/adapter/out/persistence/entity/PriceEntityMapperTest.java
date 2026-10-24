package com.gft.price.infrastructure.adapter.out.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gft.price.domain.model.Price;

class PriceEntityMapperTest {

    @Test
    @DisplayName("debe convertir todos los campos de la entidad al modelo de dominio")
    void shouldMapAllEntityFieldsToDomain() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 15, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 18, 30);
        PriceEntity entity = new PriceEntity(7L, 1, 35455, 2, startDate, endDate, 1,
                new BigDecimal("25.45"), "EUR");

        Price result = PriceEntityMapper.toDomain(entity);

        assertEquals(1, result.getBrandId());
        assertEquals(35455, result.getProductId());
        assertEquals(2, result.getTariffId());
        assertEquals(startDate, result.getStartDate());
        assertEquals(endDate, result.getEndDate());
        assertEquals(1, result.getPriority());
        assertEquals(new BigDecimal("25.45"), result.getAmount());
        assertEquals("EUR", result.getCurrencyCode());
    }
}
