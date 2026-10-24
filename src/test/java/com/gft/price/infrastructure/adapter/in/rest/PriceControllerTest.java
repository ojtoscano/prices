package com.gft.price.infrastructure.adapter.in.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;

import com.gft.price.domain.model.Price;
import com.gft.price.domain.model.PriceQuery;
import com.gft.price.domain.port.in.GetApplicablePriceUseCase;
import com.gft.price.infrastructure.adapter.in.rest.dto.PriceResponse;

@ExtendWith(MockitoExtension.class)
class PriceControllerTest {

    @Mock
    private GetApplicablePriceUseCase getApplicablePriceUseCase;

    @ParameterizedTest(name = "caso {0}: debe devolver la tarifa aplicable")
    @MethodSource("priceCases")
    @DisplayName("debe devolver la respuesta REST correspondiente a cada caso de negocio")
    void shouldReturnApplicablePrice(int caseNumber, LocalDateTime applicationDate, int expectedTariffId,
            String expectedAmount) {
        Price expectedPrice = price(expectedTariffId, expectedAmount);
        when(getApplicablePriceUseCase.getApplicablePrice(any(PriceQuery.class))).thenReturn(expectedPrice);

        ResponseEntity<PriceResponse> response = new PriceController(getApplicablePriceUseCase)
                .getApplicablePrice(applicationDate, 35455, 1);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getBrandId());
        assertEquals(35455, response.getBody().getProductId());
        assertEquals(expectedTariffId, response.getBody().getTariffId());
        assertEquals(new BigDecimal(expectedAmount), response.getBody().getAmount());

        ArgumentCaptor<PriceQuery> queryCaptor = ArgumentCaptor.forClass(PriceQuery.class);
        verify(getApplicablePriceUseCase).getApplicablePrice(queryCaptor.capture());
        assertEquals(applicationDate, queryCaptor.getValue().getApplicationDate());
        assertEquals(35455, queryCaptor.getValue().getProductId());
        assertEquals(1, queryCaptor.getValue().getBrandId());
    }

    @Test
    @DisplayName("debe propagar la excepción cuando el caso de uso no encuentra un precio")
    void shouldPropagatePriceNotFoundException() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        RuntimeException expectedException = new RuntimeException("price not found");
        when(getApplicablePriceUseCase.getApplicablePrice(any(PriceQuery.class))).thenThrow(expectedException);

        RuntimeException actualException = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> new PriceController(getApplicablePriceUseCase)
                        .getApplicablePrice(applicationDate, 35455, 1));

        assertEquals(expectedException, actualException);
    }

    private static Stream<Arguments> priceCases() {
        return Stream.of(
                Arguments.of(1, LocalDateTime.of(2020, 6, 14, 10, 0), 1, "35.50"),
                Arguments.of(2, LocalDateTime.of(2020, 6, 14, 16, 0), 2, "25.45"),
                Arguments.of(3, LocalDateTime.of(2020, 6, 14, 21, 0), 1, "35.50"),
                Arguments.of(4, LocalDateTime.of(2020, 6, 15, 10, 0), 3, "30.50"),
                Arguments.of(5, LocalDateTime.of(2020, 6, 16, 21, 0), 4, "38.95"));
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
