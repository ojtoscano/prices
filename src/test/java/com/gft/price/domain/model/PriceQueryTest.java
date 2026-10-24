package com.gft.price.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceQueryTest {

    @Test
    @DisplayName("debe crear una consulta válida")
    void shouldCreateValidQuery() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        PriceQuery query = new PriceQuery(applicationDate, 1, 35455);

        assertEquals(applicationDate, query.getApplicationDate());
        assertEquals(1, query.getBrandId());
        assertEquals(35455, query.getProductId());
    }

    @Test
    @DisplayName("debe rechazar identificadores no positivos")
    void shouldRejectNonPositiveIdentifiers() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        assertThrows(IllegalArgumentException.class, () -> new PriceQuery(applicationDate, 0, 35455));
        assertThrows(IllegalArgumentException.class, () -> new PriceQuery(applicationDate, 1, -1));
    }
}
