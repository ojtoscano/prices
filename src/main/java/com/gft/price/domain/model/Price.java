package com.gft.price.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Price {

    private final Long brandId;
    private final Integer productId;
    private final Integer tariffId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Integer priority;
    private final BigDecimal amount;
    private final String currencyCode;

    @Builder 
    public Price(Long brandId, Integer productId, Integer tariffId, 
        LocalDateTime startDate, LocalDateTime endDate, Integer priority, 
        BigDecimal amount, String currencyCode) {
        this.brandId = Objects.requireNonNull(brandId, "Brand ID cannot be null");
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
        this.tariffId = Objects.requireNonNull(tariffId, "Tariff ID cannot be null");
        this.startDate = Objects.requireNonNull(startDate, "Start date cannot be null");
        this.endDate = Objects.requireNonNull(endDate, "End date cannot be null");
        this.priority = Objects.requireNonNull(priority, "Priority cannot be null");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
        this.currencyCode = Objects.requireNonNull(currencyCode, "Currency code cannot be null");
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }
}
