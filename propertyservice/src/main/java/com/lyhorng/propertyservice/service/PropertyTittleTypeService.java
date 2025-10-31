package com.lyhorng.propertyservice.service;

import com.lyhorng.propertyservice.model.PropertyTittleType;
import com.lyhorng.propertyservice.repository.PropertyTittleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyTittleTypeService {

    @Autowired
    private PropertyTittleTypeRepository propertyTittleTypeRepository;

    // Get all Property Title Types
    public List<PropertyTittleType> getAllPropertyTitleTypes() {
        return propertyTittleTypeRepository.findAll();
    }

    // Create a new Property Title Type
    public PropertyTittleType createPropertyTittleType(String titleType, MultipartFile file) {
        PropertyTittleType propertyTittleType = new PropertyTittleType();
        propertyTittleType.setTitleType(titleType);

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

        // Save the new Property Title Type
        return propertyTittleTypeRepository.save(propertyTittleType);
    }

    // Update an existing Property Title Type
    public PropertyTittleType updatePropertyTittleType(Long id, String titleType, MultipartFile file) {
        Optional<PropertyTittleType> existingPropertyTittleType = propertyTittleTypeRepository.findById(id);
        if (existingPropertyTittleType.isPresent()) {
            PropertyTittleType propertyTittleType = existingPropertyTittleType.get();
            propertyTittleType.setTitleType(titleType);

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

            return propertyTittleTypeRepository.save(propertyTittleType);
        } else {
            throw new RuntimeException("Property Title Type not found with ID: " + id);
        }
    }

    // Delete a Property Title Type
    public void deletePropertyTittleType(Long id) {
        Optional<PropertyTittleType> propertyTittleType = propertyTittleTypeRepository.findById(id);
        if (propertyTittleType.isPresent()) {
            propertyTittleTypeRepository.delete(propertyTittleType.get());
        } else {
            throw new RuntimeException("Property Title Type not found with ID: " + id);
        }
    }

    // Get a specific Property Title Type by ID
    public PropertyTittleType getPropertyTittleTypeById(Long id) {
        return propertyTittleTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property Title Type not found with ID: " + id));
    }
}
