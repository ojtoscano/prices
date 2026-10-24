package com.gft.price.application.service;

import com.gft.price.domain.exception.PriceNotFoundException;
import com.gft.price.domain.model.Price;
import com.gft.price.domain.model.PriceQuery;
import com.gft.price.domain.port.in.GetApplicablePriceUseCase;
import com.gft.price.domain.port.out.PriceRepositoryPort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PriceService implements GetApplicablePriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    public PriceService(PriceRepositoryPort priceRepositoryPort) {
        this.priceRepositoryPort = priceRepositoryPort;
    }

    @Override
    public Price getApplicablePrice(PriceQuery priceQuery) {
        log.debug("Executing use case findApplicablePrice: productId={}, brandId={}, applicationDate={}",
                priceQuery.getProductId(), priceQuery.getBrandId(), priceQuery.getApplicationDate());

        return priceRepositoryPort.findApplicablePrice(priceQuery)
                .orElseThrow(() -> new PriceNotFoundException(
                        priceQuery.getBrandId(), priceQuery.getProductId(), priceQuery.getApplicationDate()));
    }
}
