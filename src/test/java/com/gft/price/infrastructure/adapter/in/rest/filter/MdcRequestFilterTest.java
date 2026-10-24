package com.gft.price.infrastructure.adapter.in.rest.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;

class MdcRequestFilterTest {

    private final MdcRequestFilter filter = new MdcRequestFilter();

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    @Test
    @DisplayName("debe conservar el request id recibido y devolverlo en la respuesta")
    void shouldReuseRequestIdFromHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Request-ID", "request-123");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals("request-123", response.getHeader("X-Request-ID"));
        assertNotNull(chain.getRequest());
        assertFalse(MDC.getCopyOfContextMap() != null && MDC.getCopyOfContextMap().containsKey("requestId"));
    }

    @Test
    @DisplayName("debe generar un request id cuando no se recibe en la petición")
    void shouldGenerateRequestIdWhenHeaderIsMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        String requestId = response.getHeader("X-Request-ID");
        assertNotNull(requestId);
        assertEquals(36, UUID.fromString(requestId).toString().length());
    }
}
