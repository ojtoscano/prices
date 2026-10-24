package com.gft.price.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gft.price.domain.exception.PriceNotFoundException;
import com.gft.price.domain.model.Price;
import com.gft.price.domain.model.PriceQuery;
import com.gft.price.domain.port.in.GetApplicablePriceUseCase;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:price-service-integration;DB_CLOSE_DELAY=-1")
class PriceServiceIntegrationTest {

    private final GetApplicablePriceUseCase priceService;

    @Autowired
    PriceServiceIntegrationTest(GetApplicablePriceUseCase priceService) {
        this.priceService = priceService;
    }

    @Test
    @DisplayName("debe resolver el precio usando el servicio y la base de datos H2")
    void shouldResolvePriceThroughServiceAndDatabase() {
        PriceQuery query = new PriceQuery(LocalDateTime.of(2020, 6, 14, 16, 0), 1, 35455);

        Price result = priceService.getApplicablePrice(query);

        assertEquals(1, result.getBrandId());
        assertEquals(35455, result.getProductId());
        assertEquals(2, result.getTariffId());
        assertEquals("25.45", result.getAmount().toString());
    }

    @Test
    @DisplayName("debe propagar PriceNotFoundException cuando H2 no encuentra un precio")
    void shouldThrowWhenDatabaseHasNoApplicablePrice() {
        PriceQuery query = new PriceQuery(LocalDateTime.of(2021, 1, 1, 0, 0), 1, 35455);

        assertThrows(PriceNotFoundException.class, () -> priceService.getApplicablePrice(query));
    }

}
