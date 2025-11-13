package com.lyhorng.propertyservice.service;

import com.lyhorng.propertyservice.model.*;
import com.lyhorng.propertyservice.repository.*;

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
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyTittleTypeRepository propertyTittleTypeRepository;

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;

    @Autowired
    private PropertyInfoRepository propertyInfoRepository;

    @Autowired
    private MeasureInfoRepository measureInfoRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private CommuneRepository communeRepository;

    @Autowired
    private VillageRepository villageRepository;

    // ===================== LIST ALL =====================
    @Transactional(readOnly = true)
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    // ===================== CREATE =====================
    @Transactional
    public Property createProperty(
            Integer branchRequestId,
            String oldPropertyId,
            String propertyCode,
            String propertyVersion,
            Boolean isOwner,
            String titleNumber,
            String remark,
            Long titleTypeId,
            Long propertyTypeId,
            Long propertyInfoId,
            Long measureInfoId,
            Long provinceId,
            Long districtId,
            Long communeId,
            Long villageId,
            MultipartFile file) {
        Property property = new Property();
        property.setBranchRequestId(branchRequestId);
        property.setOldPropertyId(oldPropertyId);
        property.setPropertyCode(propertyCode);
        property.setPropertyVersion(propertyVersion);
        property.setIsOwner(isOwner);
        property.setTitleNumber(titleNumber);
        property.setRemark(remark);
        property.setCreatedAt(LocalDateTime.now());
        property.setUpdatedAt(LocalDateTime.now());

        // Set relationships with null checks
        if (titleTypeId != null) {
            property.setPropertyTitleType(propertyTittleTypeRepository.findById(titleTypeId)
                    .orElseThrow(() -> new RuntimeException("Title Type not found with ID: " + titleTypeId)));
        }
        if (propertyTypeId != null) {
            property.setPropertyType(propertyTypeRepository.findById(propertyTypeId)
                    .orElseThrow(() -> new RuntimeException("Property Type not found with ID: " + propertyTypeId)));
        }
        if (propertyInfoId != null) {
            property.setPropertyInfo(propertyInfoRepository.findById(propertyInfoId)
                    .orElseThrow(() -> new RuntimeException("Property Info not found with ID: " + propertyInfoId)));
        }
        if (measureInfoId != null) {
            property.setMeasureInfo(measureInfoRepository.findById(measureInfoId)
                    .orElseThrow(() -> new RuntimeException("Measure Info not found with ID: " + measureInfoId)));
        }
        if (provinceId != null) {
            property.setProvince(provinceRepository.findById(provinceId)
                    .orElseThrow(() -> new RuntimeException("Province not found with ID: " + provinceId)));
        }
        if (districtId != null) {
            property.setDistrict(districtRepository.findById(districtId)
                    .orElseThrow(() -> new RuntimeException("District not found with ID: " + districtId)));
        }
        if (communeId != null) {
            property.setCommune(communeRepository.findById(communeId)
                    .orElseThrow(() -> new RuntimeException("Commune not found with ID: " + communeId)));
        }
        if (villageId != null) {
            property.setVillage(villageRepository.findById(villageId)
                    .orElseThrow(() -> new RuntimeException("Village not found with ID: " + villageId)));
        }

        // Handle file upload if present
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = storeFile(file);
                property.setFilePath(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Error processing file: " + e.getMessage());
            }
        }

        return propertyRepository.save(property);
    }

    // ===================== UPDATE =====================
    @Transactional
    public Property updateProperty(
            Long id,
            Integer branchRequestId,
            String oldPropertyId,
            String propertyCode,
            String propertyVersion,
            Boolean isOwner,
            String titleNumber,
            String remark,
            Long titleTypeId,
            Long propertyTypeId,
            Long propertyInfoId,
            Long measureInfoId,
            Long provinceId,
            Long districtId,
            Long communeId,
            Long villageId,
            MultipartFile file) {
        Property existing = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found with ID: " + id));

        existing.setBranchRequestId(branchRequestId);
        existing.setOldPropertyId(oldPropertyId);
        existing.setPropertyCode(propertyCode);
        existing.setPropertyVersion(propertyVersion);
        existing.setIsOwner(isOwner);
        existing.setTitleNumber(titleNumber);
        existing.setRemark(remark);
        existing.setUpdatedAt(LocalDateTime.now());

        // Update relationships with null checks
        if (titleTypeId != null) {
            existing.setPropertyTitleType(propertyTittleTypeRepository.findById(titleTypeId)
                    .orElseThrow(() -> new RuntimeException("Title Type not found with ID: " + titleTypeId)));
        }
        if (propertyTypeId != null) {
            existing.setPropertyType(propertyTypeRepository.findById(propertyTypeId)
                    .orElseThrow(() -> new RuntimeException("Property Type not found with ID: " + propertyTypeId)));
        }
        if (propertyInfoId != null) {
            existing.setPropertyInfo(propertyInfoRepository.findById(propertyInfoId)
                    .orElseThrow(() -> new RuntimeException("Property Info not found with ID: " + propertyInfoId)));
        }
        if (measureInfoId != null) {
            existing.setMeasureInfo(measureInfoRepository.findById(measureInfoId)
                    .orElseThrow(() -> new RuntimeException("Measure Info not found with ID: " + measureInfoId)));
        }
        if (provinceId != null) {
            existing.setProvince(provinceRepository.findById(provinceId)
                    .orElseThrow(() -> new RuntimeException("Province not found with ID: " + provinceId)));
        }
        if (districtId != null) {
            existing.setDistrict(districtRepository.findById(districtId)
                    .orElseThrow(() -> new RuntimeException("District not found with ID: " + districtId)));
        }
        if (communeId != null) {
            existing.setCommune(communeRepository.findById(communeId)
                    .orElseThrow(() -> new RuntimeException("Commune not found with ID: " + communeId)));
        }
        if (villageId != null) {
            existing.setVillage(villageRepository.findById(villageId)
                    .orElseThrow(() -> new RuntimeException("Village not found with ID: " + villageId)));
        }

        // Handle file upload if present
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = storeFile(file);
                existing.setFilePath(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Error processing file: " + e.getMessage());
            }
        }

        return propertyRepository.save(existing);
    }

    // ===================== DELETE =====================
    @Transactional
    public void deleteProperty(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found with ID: " + id));
        propertyRepository.delete(property);
    }

    // ===================== VIEW =====================
    @Transactional(readOnly = true)
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found with ID: " + id));
    }

    // Helper method for storing file
    private String storeFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = Paths.get("uploads/" + fileName);
        Files.createDirectories(targetLocation.getParent());
        Files.copy(file.getInputStream(), targetLocation);
        return fileName;
    }

    public Property findById(Long id) {
        return propertyRepository.findById(id).orElseThrow(() -> new RuntimeException("Property not found"));
    }
}