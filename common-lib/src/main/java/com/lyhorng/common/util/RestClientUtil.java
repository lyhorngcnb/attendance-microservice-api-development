package com.lyhorng.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lyhorng.common.exception.BusinessException;
import com.lyhorng.common.exception.ErrorCode;

import java.util.Map;

@Slf4j
public class RestClientUtil {
    
    private final RestTemplate restTemplate;
    
    public RestClientUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public <T> T get(String url, Class<T> responseType) {
        return get(url, null, null, responseType);
    }
    
    public <T> T get(String url, Map<String, String> headers, Class<T> responseType) {
        return get(url, headers, null, responseType);
    }
    
    public <T> T get(String url, Map<String, String> headers, Map<String, Object> params, Class<T> responseType) {
        try {
            HttpHeaders httpHeaders = createHeaders(headers);
            HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
            
            String fullUrl = buildUrlWithParams(url, params);
            
            log.debug("GET request to: {}", fullUrl);
            ResponseEntity<T> response = restTemplate.exchange(
                fullUrl,
                HttpMethod.GET,
                entity,
                responseType
            );
            
            return response.getBody();
        } catch (RestClientException e) {
            log.error("GET request failed: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.EXTERNAL_SERVICE_ERROR, "Failed to call external service", e);
        }
    }
    
    public <T> T post(String url, Object request, Class<T> responseType) {
        return post(url, request, null, responseType);
    }
    
    public <T> T post(String url, Object request, Map<String, String> headers, Class<T> responseType) {
        try {
            HttpHeaders httpHeaders = createHeaders(headers);
            HttpEntity<Object> entity = new HttpEntity<>(request, httpHeaders);
            
            log.debug("POST request to: {}", url);
            ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                responseType
            );
            
            return response.getBody();
        } catch (RestClientException e) {
            log.error("POST request failed: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.EXTERNAL_SERVICE_ERROR, "Failed to call external service", e);
        }
    }
    
    public <T> T put(String url, Object request, Class<T> responseType) {
        return put(url, request, null, responseType);
    }
    
    public <T> T put(String url, Object request, Map<String, String> headers, Class<T> responseType) {
        try {
            HttpHeaders httpHeaders = createHeaders(headers);
            HttpEntity<Object> entity = new HttpEntity<>(request, httpHeaders);
            
            log.debug("PUT request to: {}", url);
            ResponseEntity<T> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                responseType
            );
            
            return response.getBody();
        } catch (RestClientException e) {
            log.error("PUT request failed: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.EXTERNAL_SERVICE_ERROR, "Failed to call external service", e);
        }
    }
    
    public void delete(String url) {
        delete(url, null);
    }
    
    public void delete(String url, Map<String, String> headers) {
        try {
            HttpHeaders httpHeaders = createHeaders(headers);
            HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
            
            log.debug("DELETE request to: {}", url);
            restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                entity,
                Void.class
            );
        } catch (RestClientException e) {
            log.error("DELETE request failed: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.EXTERNAL_SERVICE_ERROR, "Failed to call external service", e);
        }
    }
    
    public <T> T exchange(String url, HttpMethod method, Object request, 
                          Map<String, String> headers, Class<T> responseType) {
        try {
            HttpHeaders httpHeaders = createHeaders(headers);
            HttpEntity<Object> entity = new HttpEntity<>(request, httpHeaders);
            
            log.debug("{} request to: {}", method, url);
            ResponseEntity<T> response = restTemplate.exchange(
                url,
                method,
                entity,
                responseType
            );
            
            return response.getBody();
        } catch (RestClientException e) {
            log.error("{} request failed: {}", method, e.getMessage(), e);
            throw new BusinessException(ErrorCode.EXTERNAL_SERVICE_ERROR, "Failed to call external service", e);
        }
    }
    
    public <T> T exchange(String url, HttpMethod method, Object request,
                          Map<String, String> headers, ParameterizedTypeReference<T> responseType) {
        try {
            HttpHeaders httpHeaders = createHeaders(headers);
            HttpEntity<Object> entity = new HttpEntity<>(request, httpHeaders);
            
            log.debug("{} request to: {}", method, url);
            ResponseEntity<T> response = restTemplate.exchange(
                url,
                method,
                entity,
                responseType
            );
            
            return response.getBody();
        } catch (RestClientException e) {
            log.error("{} request failed: {}", method, e.getMessage(), e);
            throw new BusinessException(ErrorCode.EXTERNAL_SERVICE_ERROR, "Failed to call external service", e);
        }
    }
    
    private HttpHeaders createHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpHeaders::set);
        }
        
        return httpHeaders;
    }
    
    private String buildUrlWithParams(String url, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("?");
        
        params.forEach((key, value) -> {
            urlBuilder.append(key).append("=").append(value).append("&");
        });
        
        return urlBuilder.substring(0, urlBuilder.length() - 1);
    }
}