package com.lyhorng.propertyservice.service;

import com.lyhorng.propertyservice.model.PropertyType;
import com.lyhorng.propertyservice.repository.PropertyTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyTypeService {

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;

    // Get all Property  Types
    public List<PropertyType> getAllPropertyTypes() {
        return propertyTypeRepository.findAll();
    }

    // Create a new Property  Type
    public PropertyType createPropertyType(String Type, MultipartFile file) {
        PropertyType propertyType = new PropertyType();
        propertyType.setType(Type);

        // If there is a file uploaded, you can handle the file here (optional)
        if (file != null && !file.isEmpty()) {
            try {
                // You can save the file somewhere or use it
                // For now, we're not using it, but you can save it or process it
                byte[] bytes = file.getBytes();
                // Save or use the file bytes if necessary
            } catch (Exception e) {
                // Handle any exceptions that occur during file processing
                throw new RuntimeException("Error processing the file: " + e.getMessage());
            }
        }

        // Save the new Property  Type
        return propertyTypeRepository.save(propertyType);
    }

    // Update an existing Property Type
    public PropertyType updatePropertyType(Long id, String Type, MultipartFile file) {
        Optional<PropertyType> existingPropertyType = propertyTypeRepository.findById(id);
        if (existingPropertyType.isPresent()) {
            PropertyType propertyType = existingPropertyType.get();
            propertyType.setType(Type);

            // Handle file upload (optional)
            if (file != null && !file.isEmpty()) {
                try {
                    // You can save the file or process it here
                    byte[] bytes = file.getBytes();
                    // Save or use the file bytes if necessary
                } catch (Exception e) {
                    throw new RuntimeException("Error processing the file: " + e.getMessage());
                }
            }

            return propertyTypeRepository.save(propertyType);
        } else {
            throw new RuntimeException("Property  Type not found with ID: " + id);
        }
    }

    // Delete a Property  Type
    public void deletePropertyType(Long id) {
        Optional<PropertyType> propertyType = propertyTypeRepository.findById(id);
        if (propertyType.isPresent()) {
            propertyTypeRepository.delete(propertyType.get());
        } else {
            throw new RuntimeException("Property  Type not found with ID: " + id);
        }
    }

    // Get a specific Property  Type by ID
    public PropertyType getPropertyTypeById(Long id) {
        return propertyTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property  Type not found with ID: " + id));
    }
}
