package com.gft.price.infrastructure.adapter.in.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:price-controller-integration;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
class PriceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "caso {0}: {1} debe devolver la tarifa {2}")
    @MethodSource("priceCases")
    @DisplayName("debe resolver el precio correcto a través del endpoint REST")
    void shouldReturnApplicablePriceThroughRestEndpoint(int caseNumber, LocalDateTime applicationDate,
            int expectedTariffId, String expectedAmount, String expectedStartDate, String expectedEndDate)
            throws Exception {

        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", applicationDate.toString())
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Request-ID"))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.tariffId").value(expectedTariffId))
                .andExpect(jsonPath("$.startDate").value(expectedStartDate))
                .andExpect(jsonPath("$.endDate").value(expectedEndDate))
                .andExpect(jsonPath("$.amount").value(Double.parseDouble(expectedAmount)))
                .andExpect(jsonPath("$.currencyCode").value("EUR"));
    }

    @ParameterizedTest(name = "{0}={1} debe devolver 400")
    @CsvSource({
            "productId, 0",
            "brandId, -1"
    })
    @DisplayName("debe rechazar identificadores no positivos")
    void shouldReturnBadRequestForNonPositiveIdentifiers(String parameterName, String parameterValue)
            throws Exception {

        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", parameterName.equals("productId") ? parameterValue : "35455")
                .param("brandId", parameterName.equals("brandId") ? parameterValue : "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message", Matchers.containsString(parameterName)))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("debe devolver 404 cuando no existe un precio aplicable")
    void shouldReturnNotFoundWhenPriceDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2021-01-01T00:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value(
                        "No applicable price found for brandId=1, productId=35455 at 2021-01-01T00:00"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("debe devolver 400 cuando la fecha no tiene un formato válido")
    void shouldReturnBadRequestForInvalidDate() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "invalid-date")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value(
                        "Invalid value for parameter 'applicationDate': 'invalid-date'"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("debe devolver 400 cuando falta un parámetro obligatorio")
    void shouldReturnBadRequestForMissingParameter() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("productId", "35455")
                .param("brandId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Missing required parameter: 'applicationDate'"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    private static Stream<Arguments> priceCases() {
        return Stream.of(
                Arguments.of(1, LocalDateTime.of(2020, 6, 14, 10, 0), 1, "35.50",
                        "2020-06-14T00:00:00", "2020-12-31T23:59:59"),
                Arguments.of(2, LocalDateTime.of(2020, 6, 14, 16, 0), 2, "25.45",
                        "2020-06-14T15:00:00", "2020-06-14T18:30:00"),
                Arguments.of(3, LocalDateTime.of(2020, 6, 14, 21, 0), 1, "35.50",
                        "2020-06-14T00:00:00", "2020-12-31T23:59:59"),
                Arguments.of(4, LocalDateTime.of(2020, 6, 15, 10, 0), 3, "30.50",
                        "2020-06-15T00:00:00", "2020-06-15T11:00:00"),
                Arguments.of(5, LocalDateTime.of(2020, 6, 16, 21, 0), 4, "38.95",
                        "2020-06-15T16:00:00", "2020-12-31T23:59:59"));
    }
}
