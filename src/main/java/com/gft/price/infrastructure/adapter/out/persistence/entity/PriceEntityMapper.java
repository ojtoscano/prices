package com.gft.price.infrastructure.adapter.out.persistence.entity;

import com.gft.price.domain.model.Price;

public class PriceEntityMapper {

    private PriceEntityMapper() {
        // Private constructor to prevent instantiation
    }
    
    public static Price toDomain(PriceEntity priceEntity) {
        return Price.builder()
            .brandId(priceEntity.getBrandId())
            .productId(priceEntity.getProductId())
            .tariffId(priceEntity.getTariffId())
            .startDate(priceEntity.getStartDate())
            .endDate(priceEntity.getEndDate())
            .priority(priceEntity.getPriority())
            .amount(priceEntity.getAmount())
            .currencyCode(priceEntity.getCurrencyCode())
            .build();
    }
}
