package com.lyhorng.propertyservice.service;

import com.lyhorng.propertyservice.model.PropertyMeasureInfo;
import com.lyhorng.propertyservice.repository.MeasureInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class MeasureInfoService {

    @Autowired
    private MeasureInfoRepository measureInfoRepository;

    // Get all Measure Info
    public List<PropertyMeasureInfo> getAllMeasureInfo() {
        return measureInfoRepository.findAll();
    }

    // Create a new Property Measure Info
    public PropertyMeasureInfo createMeasureInfo(String type, MultipartFile file) {
        PropertyMeasureInfo propertyMeasureInfo = new PropertyMeasureInfo();
        propertyMeasureInfo.setType(type);

        // If there is a file uploaded, you can handle the file here (optional)
        if (file != null && !file.isEmpty()) {
            try {
                // You can save the file somewhere or use it
                byte[] bytes = file.getBytes();
                // Save or process the file bytes if necessary
            } catch (Exception e) {
                // Handle any exceptions that occur during file processing
                throw new RuntimeException("Error processing the file: " + e.getMessage());
            }
        }

        // Save the new Property Measure Info
        return measureInfoRepository.save(propertyMeasureInfo);
    }

    // Update an existing Property Measure Info
    public PropertyMeasureInfo updateMeasureInfo(Long id, String type, MultipartFile file) {
        Optional<PropertyMeasureInfo> existingMeasureInfo = measureInfoRepository.findById(id);
        if (existingMeasureInfo.isPresent()) {
            PropertyMeasureInfo propertyMeasureInfo = existingMeasureInfo.get();
            propertyMeasureInfo.setType(type);

            // Handle file upload (optional)
            if (file != null && !file.isEmpty()) {
                try {
                    // You can save the file or process it here
                    byte[] bytes = file.getBytes();
                    // Save or process the file bytes if necessary
                } catch (Exception e) {
                    throw new RuntimeException("Error processing the file: " + e.getMessage());
                }
            }

            return measureInfoRepository.save(propertyMeasureInfo);
        } else {
            throw new RuntimeException("Property Measure Info not found with ID: " + id);
        }
    }

    // Delete a Property Measure Info
    public void deleteMeasureInfo(Long id) {
        Optional<PropertyMeasureInfo> propertyMeasureInfo = measureInfoRepository.findById(id);
        if (propertyMeasureInfo.isPresent()) {
            measureInfoRepository.delete(propertyMeasureInfo.get());
        } else {
            throw new RuntimeException("Property Measure Info not found with ID: " + id);
        }
    }

    // Get a specific Property Measure Info by ID
    public PropertyMeasureInfo getMeasureInfoById(Long id) {
        return measureInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property Measure Info not found with ID: " + id));
    }
}
