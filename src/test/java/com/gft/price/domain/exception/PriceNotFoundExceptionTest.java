package com.gft.price.domain.exception;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceNotFoundExceptionTest {

    @Test
    void shouldIncludeBrandIdProductIdAndApplicationDateInMessage() {
        Integer brandId = 1;
        Integer productId = 35455;
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        PriceNotFoundException exception = new PriceNotFoundException(brandId, productId, applicationDate);

        assertEquals("No applicable price found for brandId=1, productId=35455 at 2020-06-14T10:00",
                exception.getMessage());
    }
}
