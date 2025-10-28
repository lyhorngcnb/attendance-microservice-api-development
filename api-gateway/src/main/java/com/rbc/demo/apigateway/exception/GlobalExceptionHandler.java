// ========================================
// 5. GlobalExceptionHandler.java
// Location: src/main/java/.../apigateway/exception/GlobalExceptionHandler.java
// ========================================
package com.rbc.demo.apigateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@Slf4j
public class GlobalExceptionHandler extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
        
        Throwable error = getError(request);
        
        // Custom error response
        errorAttributes.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        errorAttributes.put("path", request.path());
        errorAttributes.put("method", request.methodName());
        
        if (error instanceof ResponseStatusException) {
            ResponseStatusException rse = (ResponseStatusException) error;
            errorAttributes.put("status", rse.getStatusCode().value());
            errorAttributes.put("error", rse.getStatusCode().toString());
            errorAttributes.put("message", rse.getReason());
        } else {
            errorAttributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorAttributes.put("error", "Internal Server Error");
            errorAttributes.put("message", error != null ? error.getMessage() : "Unknown error");
        }
        
        log.error("Gateway Error: {} - {}", errorAttributes.get("status"), errorAttributes.get("message"));
        
        return errorAttributes;
    }
}
