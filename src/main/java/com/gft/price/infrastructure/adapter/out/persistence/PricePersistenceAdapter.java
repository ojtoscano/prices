package com.gft.price.infrastructure.adapter.out.persistence;

import java.util.Optional;

import com.gft.price.domain.model.Price;
import com.gft.price.domain.model.PriceQuery;
import com.gft.price.domain.port.out.PriceRepositoryPort;
import com.gft.price.infrastructure.adapter.out.persistence.repository.PriceJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PricePersistenceAdapter implements PriceRepositoryPort {

    private final PriceJpaRepository priceJpaRepository;

    public PricePersistenceAdapter(PriceJpaRepository priceJpaRepository) {
        this.priceJpaRepository = priceJpaRepository;
    }

    @Override
    public Optional<Price> findApplicablePrice(PriceQuery priceQuery) {
        log.debug("Querying price: brandId={}, productId={}, applicationDate={}",
                priceQuery.getBrandId(), priceQuery.getProductId(), priceQuery.getApplicationDate());
        Optional<Price> price = priceJpaRepository.findTopByBrandAndProductAndDate(
                priceQuery.getBrandId(), priceQuery.getProductId(), priceQuery.getApplicationDate())
                .map(PriceEntityMapper::toDomain);
        log.debug("Query result: brandId={}, productId={} -> {}", priceQuery.getBrandId(), priceQuery.getProductId(),
                price.isPresent() ? "found (tariffId=" + price.get().getTariffId() + ")" : "not found");
        return price;
    }
}
