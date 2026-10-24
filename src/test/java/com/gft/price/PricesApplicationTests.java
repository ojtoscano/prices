package com.gft.price;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:prices-application-test;DB_CLOSE_DELAY=-1")
class PricesApplicationTests {

    @Test
    @DisplayName("debe cargar correctamente el contexto de Spring")
    void contextLoads() {
	}

}
