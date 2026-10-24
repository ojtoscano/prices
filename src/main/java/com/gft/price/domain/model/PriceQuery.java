package com.gft.price.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Getter;

@Getter
public class PriceQuery {

    private final LocalDateTime applicationDate;
    private final Integer brandId;
    private final Integer productId;

    public PriceQuery(LocalDateTime applicationDate, Integer brandId, Integer productId) {
        this.applicationDate = Objects.requireNonNull(applicationDate, "Application date cannot be null");
        this.brandId = Objects.requireNonNull(brandId, "Brand ID cannot be null");
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
    }
}
