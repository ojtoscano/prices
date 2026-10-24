package com.gft.price.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Price {

    private final Integer brandId;
    private final Integer productId;
    private final Integer tariffId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Integer priority;
    private final BigDecimal amount;
    private final String currencyCode;

    public Price(Integer brandId, Integer productId, Integer tariffId,
            LocalDateTime startDate, LocalDateTime endDate, Integer priority,
            BigDecimal amount, String currencyCode) {
        this.brandId = requirePositive(brandId, "Brand ID cannot be null", "Brand ID must be positive");
        this.productId = requirePositive(productId, "Product ID cannot be null", "Product ID must be positive");
        this.tariffId = requirePositive(tariffId, "Tariff ID cannot be null", "Tariff ID must be positive");
        this.startDate = Objects.requireNonNull(startDate, "Start date cannot be null");
        this.endDate = Objects.requireNonNull(endDate, "End date cannot be null");
        this.priority = Objects.requireNonNull(priority, "Priority cannot be null");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
        this.currencyCode = Objects.requireNonNull(currencyCode, "Currency code cannot be null");
        if (priority < 0) {
            throw new IllegalArgumentException("Priority cannot be negative");
        }
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    public Integer getBrandId() {
        return brandId;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getTariffId() {
        return tariffId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Integer brandId;
        private Integer productId;
        private Integer tariffId;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer priority;
        private BigDecimal amount;
        private String currencyCode;

        private Builder() {
        }

        public Builder brandId(Integer brandId) {
            this.brandId = brandId;
            return this;
        }

        public Builder productId(Integer productId) {
            this.productId = productId;
            return this;
        }

        public Builder tariffId(Integer tariffId) {
            this.tariffId = tariffId;
            return this;
        }

        public Builder startDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder currencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public Price build() {
            return new Price(brandId, productId, tariffId, startDate, endDate,
                    priority, amount, currencyCode);
        }
    }

    private static Integer requirePositive(Integer value, String nullMessage, String positiveMessage) {
        Objects.requireNonNull(value, nullMessage);
        if (value <= 0) {
            throw new IllegalArgumentException(positiveMessage);
        }
        return value;
    }
}
