package com.gft.price.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    @DisplayName("debe crear un precio válido y exponer sus datos")
    void shouldCreateValidPrice() {
        Price price = validPrice();

        assertEquals(1, price.getBrandId());
        assertEquals(35455, price.getProductId());
        assertEquals(2, price.getTariffId());
        assertEquals(1, price.getPriority());
        assertEquals(new BigDecimal("25.45"), price.getAmount());
        assertEquals("EUR", price.getCurrencyCode());
    }

    @Test
    @DisplayName("debe rechazar un rango de fechas invertido")
    void shouldRejectInvertedDateRange() {
        assertThrows(IllegalArgumentException.class, () -> Price.builder()
                .brandId(1)
                .productId(35455)
                .tariffId(2)
                .startDate(LocalDateTime.of(2020, 6, 15, 0, 0))
                .endDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .priority(1)
                .amount(new BigDecimal("25.45"))
                .currencyCode("EUR")
                .build());
    }

    @Test
    @DisplayName("debe rechazar un importe negativo")
    void shouldRejectNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> validPriceBuilder()
                .amount(new BigDecimal("-0.01"))
                .build());
    }

    @Test
    @DisplayName("debe rechazar una prioridad negativa")
    void shouldRejectNegativePriority() {
        assertThrows(IllegalArgumentException.class, () -> validPriceBuilder()
                .priority(-1)
                .build());
    }

    @Test
    @DisplayName("debe rechazar identificadores no positivos")
    void shouldRejectNonPositiveIdentifiers() {
        assertThrows(IllegalArgumentException.class, () -> validPriceBuilder().brandId(0).build());
        assertThrows(IllegalArgumentException.class, () -> validPriceBuilder().productId(-1).build());
        assertThrows(IllegalArgumentException.class, () -> validPriceBuilder().tariffId(0).build());
    }

    private static Price validPrice() {
        return validPriceBuilder().build();
    }

    private static Price.Builder validPriceBuilder() {
        return Price.builder()
                .brandId(1)
                .productId(35455)
                .tariffId(2)
                .startDate(LocalDateTime.of(2020, 6, 14, 15, 0))
                .endDate(LocalDateTime.of(2020, 6, 14, 18, 30))
                .priority(1)
                .amount(new BigDecimal("25.45"))
                .currencyCode("EUR");
    }
}
