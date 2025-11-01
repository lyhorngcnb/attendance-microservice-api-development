package com.lyhorng.propertyservice.service;

import com.lyhorng.propertyservice.model.Property;
import com.lyhorng.propertyservice.model.PropertyMap;
import com.lyhorng.propertyservice.repository.PropertyMapRepository;
import com.lyhorng.propertyservice.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PropertyMapService {

    @Autowired
    private PropertyMapRepository propertyMapRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // ===================== LIST ALL =====================
    @Transactional(readOnly = true)
    public List<PropertyMap> getAllPropertyMaps() {
        return propertyMapRepository.findAll();
    }

    // ===================== GET BY PROPERTY ID =====================
    @Transactional(readOnly = true)
    public List<PropertyMap> getMapsByPropertyId(Long propertyId) {
        return propertyMapRepository.findByPropertyId(propertyId);
    }

    // ===================== GET BY ID =====================
    @Transactional(readOnly = true)
    public PropertyMap getPropertyMapById(Long id) {
        return propertyMapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property map not found with ID: " + id));
    }

    // ===================== CREATE =====================
    @Transactional
    public PropertyMap createPropertyMap(
            String mapData,
            String color,
            Double latitude,
            Double longitude,
            Long propertyId,
            MultipartFile attachment) {
        
        // Validate property exists
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found with ID: " + propertyId));

        PropertyMap propertyMap = new PropertyMap();
        propertyMap.setMapData(mapData);
        propertyMap.setColor(color);
        propertyMap.setLatitude(latitude);
        propertyMap.setLongitude(longitude);
        propertyMap.setProperty(property);
        propertyMap.setCreatedAt(LocalDateTime.now());
        propertyMap.setUpdatedAt(LocalDateTime.now());

        // Handle attachment upload if present
        if (attachment != null && !attachment.isEmpty()) {
            try {
                String fileName = storeFile(attachment);
                propertyMap.setAttachment(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Error processing attachment: " + e.getMessage());
            }
        }

        return propertyMapRepository.save(propertyMap);
    }

    // ===================== UPDATE =====================
    @Transactional
    public PropertyMap updatePropertyMap(
            Long id,
            String mapData,
            String color,
            Double latitude,
            Double longitude,
            Long propertyId,
            MultipartFile attachment) {
        
        PropertyMap existing = propertyMapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property map not found with ID: " + id));

        // Update fields
        if (mapData != null) existing.setMapData(mapData);
        if (color != null) existing.setColor(color);
        if (latitude != null) existing.setLatitude(latitude);
        if (longitude != null) existing.setLongitude(longitude);
        
        // Update property if changed
        if (propertyId != null && !propertyId.equals(existing.getPropertyId())) {
            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new RuntimeException("Property not found with ID: " + propertyId));
            existing.setProperty(property);
        }

        // Handle attachment upload if present
        if (attachment != null && !attachment.isEmpty()) {
            try {
                String fileName = storeFile(attachment);
                existing.setAttachment(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Error processing attachment: " + e.getMessage());
            }
        }

        existing.setUpdatedAt(LocalDateTime.now());
        return propertyMapRepository.save(existing);
    }

    // ===================== DELETE =====================
    @Transactional
    public void deletePropertyMap(Long id) {
        PropertyMap propertyMap = propertyMapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property map not found with ID: " + id));
        
        // Optional: Delete attachment file if exists
        if (propertyMap.getAttachment() != null) {
            deleteFile(propertyMap.getAttachment());
        }
        
        propertyMapRepository.delete(propertyMap);
    }

    // ===================== DELETE BY PROPERTY ID =====================
    @Transactional
    public void deleteMapsByPropertyId(Long propertyId) {
        propertyMapRepository.deleteByPropertyId(propertyId);
    }

    // ===================== GET MAPS IN BOUNDING BOX =====================
    @Transactional(readOnly = true)
    public List<PropertyMap> getMapsInBoundingBox(
            Double minLat, Double maxLat, Double minLng, Double maxLng) {
        return propertyMapRepository.findByLatitudeBetweenAndLongitudeBetween(
            minLat, maxLat, minLng, maxLng
        );
    }

    // ===================== HELPER: STORE FILE =====================
    private String storeFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = Paths.get("uploads/maps/" + fileName);
        Files.createDirectories(targetLocation.getParent());
        Files.copy(file.getInputStream(), targetLocation);
        return fileName;
    }

    // ===================== HELPER: DELETE FILE =====================
    private void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get("uploads/maps/" + fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log error but don't throw exception
            System.err.println("Failed to delete file: " + fileName);
        }
    }
}