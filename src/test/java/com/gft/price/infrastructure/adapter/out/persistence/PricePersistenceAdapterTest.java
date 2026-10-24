package com.gft.price.infrastructure.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.gft.price.domain.model.Price;
import com.gft.price.domain.model.PriceQuery;
import com.gft.price.infrastructure.adapter.out.persistence.entity.PriceEntity;
import com.gft.price.infrastructure.adapter.out.persistence.repository.PriceJpaRepository;

@ExtendWith(MockitoExtension.class)
class PricePersistenceAdapterTest {

    @Mock
    private PriceJpaRepository priceJpaRepository;

    @Test
    @DisplayName("debe devolver el precio de dominio cuando el repositorio encuentra una tarifa aplicable")
    void shouldReturnMappedDomainPriceWhenRepositoryFindsPrice() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        PriceQuery query = new PriceQuery(applicationDate, 1, 35455);
        PriceEntity entity = new PriceEntity(2L, 1, 35455, 2,
                LocalDateTime.of(2020, 6, 14, 15, 0), LocalDateTime.of(2020, 6, 14, 18, 30), 1,
                new BigDecimal("25.45"), "EUR");
        when(priceJpaRepository.findTopByBrandAndProductAndDate(1, 35455, applicationDate))
                .thenReturn(Optional.of(entity));

        Optional<Price> result = new PricePersistenceAdapter(priceJpaRepository).findApplicablePrice(query);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().getTariffId());
        assertEquals(new BigDecimal("25.45"), result.get().getAmount());
        verify(priceJpaRepository).findTopByBrandAndProductAndDate(1, 35455, applicationDate);
    }

    @Test
    @DisplayName("debe devolver vacío cuando no existe una tarifa aplicable")
    void shouldReturnEmptyWhenRepositoryFindsNoPrice() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        PriceQuery query = new PriceQuery(applicationDate, 1, 35455);
        when(priceJpaRepository.findTopByBrandAndProductAndDate(1, 35455, applicationDate))
                .thenReturn(Optional.empty());

        Optional<Price> result = new PricePersistenceAdapter(priceJpaRepository).findApplicablePrice(query);

        assertTrue(result.isEmpty());
        verify(priceJpaRepository).findTopByBrandAndProductAndDate(1, 35455, applicationDate);
    }
}
