package com.gft.price.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gft.price.application.service.PriceService;
import com.gft.price.domain.port.in.GetApplicablePriceUseCase;
import com.gft.price.domain.port.out.PriceRepositoryPort;
import com.gft.price.infrastructure.adapter.out.persistence.PricePersistenceAdapter;
import com.gft.price.infrastructure.adapter.out.persistence.repository.PriceJpaRepository;

@Configuration
public class ApplicationConfig {

    @Bean
    PriceRepositoryPort priceRepositoryPort(PriceJpaRepository jpaRepository) {
        return new PricePersistenceAdapter(jpaRepository);
    }

    @Bean
    GetApplicablePriceUseCase getApplicablePriceUseCase(PriceRepositoryPort priceRepositoryPort) {
        return new PriceService(priceRepositoryPort);
    }
}
