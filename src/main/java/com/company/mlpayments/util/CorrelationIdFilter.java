package com.company.mlpayments.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String id = ((HttpServletRequest) request).getHeader("X-Correlation-Id");
        MDC.put("correlationId", id == null ? UUID.randomUUID().toString() : id);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
