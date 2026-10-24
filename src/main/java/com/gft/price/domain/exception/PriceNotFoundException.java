package com.gft.price.domain.exception;

import java.time.LocalDateTime;

public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(Integer brandId, Integer productId, LocalDateTime applicationDate) {
        super(String.format("No applicable price found for brandId=%d, productId=%d at %s",
                brandId, productId, applicationDate));
    }
}
