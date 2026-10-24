package com.gft.price.infrastructure.adapter.in.rest.mapper;

import com.gft.price.domain.model.Price;
import com.gft.price.infrastructure.adapter.in.rest.dto.PriceResponse;

public class PriceResponseMapper {
    
    private PriceResponseMapper() {
        // Private constructor to prevent instantiation
    }

    public static PriceResponse toResponse(Price price) {
        return PriceResponse.builder()
                .brandId(price.getBrandId())
                .productId(price.getProductId())
                .tariffId(price.getTariffId())
                .startDate(price.getStartDate())
                .endDate(price.getEndDate())
                .amount(price.getAmount())
                .currencyCode(price.getCurrencyCode())
                .build();
    }
}
