package com.lyhorng.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JsonUtils {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JSON: {}", e.getMessage(), e);
            return null;
        }
    }
    
    public static String toJsonPretty(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to pretty JSON: {}", e.getMessage(), e);
            return null;
        }
    }
    
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("Error parsing JSON to object: {}", e.getMessage(), e);
            return null;
        }
    }
    
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            log.error("Error parsing JSON to object: {}", e.getMessage(), e);
            return null;
        }
    }
    
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        if (fromValue == null) {
            return null;
        }
        try {
            return objectMapper.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            log.error("Error converting value: {}", e.getMessage(), e);
            return null;
        }
    }
    
    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        if (fromValue == null) {
            return null;
        }
        try {
            return objectMapper.convertValue(fromValue, toValueTypeRef);
        } catch (IllegalArgumentException e) {
            log.error("Error converting value: {}", e.getMessage(), e);
            return null;
        }
    }
    
    public static boolean isValidJson(String json) {
        if (json == null || json.isEmpty()) {
            return false;
        }
        try {
            objectMapper.readTree(json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}