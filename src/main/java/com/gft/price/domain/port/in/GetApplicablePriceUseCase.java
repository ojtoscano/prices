package com.gft.price.domain.port.in;

import com.gft.price.domain.model.Price;
import com.gft.price.domain.model.PriceQuery;

public interface GetApplicablePriceUseCase {
    
    Price getApplicablePrice(PriceQuery priceQuery);
}
