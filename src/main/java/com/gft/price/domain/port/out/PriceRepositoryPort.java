package com.gft.price.domain.port.out;

import java.util.Optional;

import com.gft.price.domain.model.Price;
import com.gft.price.domain.model.PriceQuery;

public interface PriceRepositoryPort {

    Optional<Price> findApplicablePrice(PriceQuery priceQuery);    
}
