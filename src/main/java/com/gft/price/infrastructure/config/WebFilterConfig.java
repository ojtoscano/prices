package com.gft.price.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gft.price.infrastructure.adapter.in.rest.filter.MdcRequestFilter;

@Configuration
public class WebFilterConfig {
    @Bean
    MdcRequestFilter mdcRequestFilter() {
        return new MdcRequestFilter();
    }
}
