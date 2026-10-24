package com.gft.price.infrastructure.adapter.in.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PriceResponse {

    private final Integer brandId;
    private final Integer productId;
    private final Integer tariffId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final BigDecimal amount;
    private final String currencyCode;
}
