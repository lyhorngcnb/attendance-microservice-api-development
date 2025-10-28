package com.lyhorng.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Filter for logging HTTP requests and responses
 */
@Slf4j
@Component
@Order(1)
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();

        // Log request
        logRequest(httpRequest, requestId);

        try {
            chain.doFilter(request, response);
        } finally {
            // Log response
            long duration = System.currentTimeMillis() - startTime;
            logResponse(httpRequest, httpResponse, duration, requestId);
        }
    }

    private void logRequest(HttpServletRequest request, String requestId) {
        if (log.isDebugEnabled()) {
            StringBuilder logMessage = new StringBuilder();
            logMessage.append("\n=== Incoming Request ===\n");
            logMessage.append("Request ID: ").append(requestId).append("\n");
            logMessage.append("Method: ").append(request.getMethod()).append("\n");
            logMessage.append("URI: ").append(request.getRequestURI()).append("\n");
            
            String queryString = request.getQueryString();
            if (queryString != null) {
                logMessage.append("Query: ").append(queryString).append("\n");
            }
            
            logMessage.append("Headers:\n");
            Collections.list(request.getHeaderNames()).forEach(headerName -> 
                logMessage.append("  ").append(headerName).append(": ")
                    .append(request.getHeader(headerName)).append("\n"));
            
            logMessage.append("=========================\n");
            
            log.debug(logMessage.toString());
        }
    }

    private void logResponse(HttpServletRequest request, HttpServletResponse response, 
                            long duration, String requestId) {
        if (log.isDebugEnabled()) {
            StringBuilder logMessage = new StringBuilder();
            logMessage.append("\n=== Outgoing Response ===\n");
            logMessage.append("Request ID: ").append(requestId).append("\n");
            logMessage.append("Method: ").append(request.getMethod()).append("\n");
            logMessage.append("URI: ").append(request.getRequestURI()).append("\n");
            logMessage.append("Status: ").append(response.getStatus()).append("\n");
            logMessage.append("Duration: ").append(duration).append("ms\n");
            
            Collection<String> headerNames = response.getHeaderNames();
            if (!headerNames.isEmpty()) {
                logMessage.append("Response Headers:\n");
                headerNames.forEach(headerName -> 
                    logMessage.append("  ").append(headerName).append(": ")
                        .append(response.getHeader(headerName)).append("\n"));
            }
            
            logMessage.append("==========================\n");
            
            log.debug(logMessage.toString());
        } else if (log.isInfoEnabled()) {
            log.info("{} {} {} - {}ms", 
                request.getMethod(), 
                request.getRequestURI(), 
                response.getStatus(), 
                duration);
        }
    }
}

