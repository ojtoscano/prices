package com.gft.price.infrastructure.adapter.in.rest.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gft.price.domain.model.Price;
import com.gft.price.infrastructure.adapter.in.rest.dto.PriceResponse;

class PriceResponseMapperTest {

    @Test
    @DisplayName("debe convertir todos los campos del dominio en la respuesta REST")
    void shouldMapAllDomainFieldsToResponse() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 15, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 18, 30);
        Price price = Price.builder()
                .brandId(1)
                .productId(35455)
                .tariffId(2)
                .startDate(startDate)
                .endDate(endDate)
                .priority(1)
                .amount(new BigDecimal("25.45"))
                .currencyCode("EUR")
                .build();

        PriceResponse response = PriceResponseMapper.toResponse(price);

        assertEquals(1, response.getBrandId());
        assertEquals(35455, response.getProductId());
        assertEquals(2, response.getTariffId());
        assertEquals(startDate, response.getStartDate());
        assertEquals(endDate, response.getEndDate());
        assertEquals(new BigDecimal("25.45"), response.getAmount());
        assertEquals("EUR", response.getCurrencyCode());
    }
}
