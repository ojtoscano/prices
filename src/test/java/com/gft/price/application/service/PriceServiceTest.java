package com.gft.price.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gft.price.domain.exception.PriceNotFoundException;
import com.gft.price.domain.model.Price;
import com.gft.price.domain.model.PriceQuery;
import com.gft.price.domain.port.out.PriceRepositoryPort;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Mock
    private PriceRepositoryPort priceRepositoryPort;

    @Test
    @DisplayName("debe devolver el precio encontrado por el repositorio")
    void shouldReturnPriceFoundByRepository() {
        PriceQuery query = queryAt(LocalDateTime.of(2020, 6, 14, 16, 0));
        Price expectedPrice = price(2, "25.45");
        when(priceRepositoryPort.findApplicablePrice(query)).thenReturn(Optional.of(expectedPrice));

        Price result = new PriceService(priceRepositoryPort).getApplicablePrice(query);

        assertSame(expectedPrice, result);
        verify(priceRepositoryPort).findApplicablePrice(query);
    }

    @Test
    @DisplayName("debe lanzar PriceNotFoundException cuando no existe un precio aplicable")
    void shouldThrowPriceNotFoundExceptionWhenRepositoryReturnsEmpty() {
        LocalDateTime applicationDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        PriceQuery query = queryAt(applicationDate);
        when(priceRepositoryPort.findApplicablePrice(query)).thenReturn(Optional.empty());

        PriceNotFoundException exception = assertThrows(PriceNotFoundException.class,
                () -> new PriceService(priceRepositoryPort).getApplicablePrice(query));

        assertEquals("No applicable price found for brandId=1, productId=35455 at 2021-01-01T00:00",
                exception.getMessage());
        verify(priceRepositoryPort).findApplicablePrice(query);
    }

    private static PriceQuery queryAt(LocalDateTime applicationDate) {
        return new PriceQuery(applicationDate, 1, 35455);
    }

    private static Price price(int tariffId, String amount) {
        return Price.builder()
                .brandId(1)
                .productId(35455)
                .tariffId(tariffId)
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59, 59))
                .priority(tariffId == 1 ? 0 : 1)
                .amount(new BigDecimal(amount))
                .currencyCode("EUR")
                .build();
    }
}
