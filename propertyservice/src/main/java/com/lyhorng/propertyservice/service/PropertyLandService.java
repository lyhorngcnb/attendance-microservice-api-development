package com.lyhorng.propertyservice.service;

import com.lyhorng.propertyservice.model.Property;
import com.lyhorng.propertyservice.model.PropertyLand;
import com.lyhorng.propertyservice.model.PropertyUnitType;
import com.lyhorng.propertyservice.repository.PropertyLandRepository;
import com.lyhorng.propertyservice.repository.PropertyRepository;
import com.lyhorng.propertyservice.repository.PropertyUnitTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PropertyLandService {

    @Autowired
    private PropertyLandRepository propertyLandRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyUnitTypeRepository propertyUnitTypeRepository;

    // ===================== LIST ALL =====================
    @Transactional(readOnly = true)
    public List<PropertyLand> getAllPropertyLands() {
        return propertyLandRepository.findAll();
    }

    // ===================== GET BY PROPERTY ID =====================
    @Transactional(readOnly = true)
    public List<PropertyLand> getLandsByPropertyId(Long propertyId) {
        return propertyLandRepository.findByPropertyId(propertyId);
    }

    // ===================== GET BY ID =====================
    @Transactional(readOnly = true)
    public PropertyLand getPropertyLandById(Long id) {
        return propertyLandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property land not found with ID: " + id));
    }

    // ===================== GET BY LOT NUMBER =====================
    @Transactional(readOnly = true)
    public PropertyLand getByLotNumber(String lotNumber) {
        return propertyLandRepository.findByLotNumber(lotNumber)
                .orElseThrow(() -> new RuntimeException("Property land not found with lot number: " + lotNumber));
    }

    // ===================== CREATE =====================
    @Transactional
    public PropertyLand createPropertyLand(
            BigDecimal landSize,
            BigDecimal front,
            BigDecimal back,
            BigDecimal length,
            BigDecimal width,
            String dimensionLand,
            String lotNumber,
            Long propertyUnitTypeId,
            Long propertyId) {
        
        // Validate property exists
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found with ID: " + propertyId));

        PropertyLand propertyLand = new PropertyLand();
        propertyLand.setLandSize(landSize);
        propertyLand.setFront(front);
        propertyLand.setBack(back);
        propertyLand.setLength(length);
        propertyLand.setWidth(width);
        propertyLand.setDimensionLand(dimensionLand);
        propertyLand.setLotNumber(lotNumber);
        propertyLand.setProperty(property);
        
        // Set unit type if provided
        if (propertyUnitTypeId != null) {
            PropertyUnitType unitType = propertyUnitTypeRepository.findById(propertyUnitTypeId)
                    .orElseThrow(() -> new RuntimeException("Property unit type not found with ID: " + propertyUnitTypeId));
            propertyLand.setPropertyUnitType(unitType);
        }
        
        propertyLand.setCreatedAt(LocalDateTime.now());
        propertyLand.setUpdatedAt(LocalDateTime.now());

        return propertyLandRepository.save(propertyLand);
    }

    // ===================== UPDATE =====================
    @Transactional
    public PropertyLand updatePropertyLand(
            Long id,
            BigDecimal landSize,
            BigDecimal front,
            BigDecimal back,
            BigDecimal length,
            BigDecimal width,
            String dimensionLand,
            String lotNumber,
            Long propertyUnitTypeId,
            Long propertyId) {
        
        PropertyLand existing = propertyLandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property land not found with ID: " + id));

        // Update fields
        if (landSize != null) existing.setLandSize(landSize);
        if (front != null) existing.setFront(front);
        if (back != null) existing.setBack(back);
        if (length != null) existing.setLength(length);
        if (width != null) existing.setWidth(width);
        if (dimensionLand != null) existing.setDimensionLand(dimensionLand);
        if (lotNumber != null) existing.setLotNumber(lotNumber);
        
        // Update property if changed
        if (propertyId != null && !propertyId.equals(existing.getPropertyId())) {
            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new RuntimeException("Property not found with ID: " + propertyId));
            existing.setProperty(property);
        }
        
        // Update unit type if provided
        if (propertyUnitTypeId != null && !propertyUnitTypeId.equals(existing.getPropertyUnitTypeId())) {
            PropertyUnitType unitType = propertyUnitTypeRepository.findById(propertyUnitTypeId)
                    .orElseThrow(() -> new RuntimeException("Property unit type not found with ID: " + propertyUnitTypeId));
            existing.setPropertyUnitType(unitType);
        }

        existing.setUpdatedAt(LocalDateTime.now());
        return propertyLandRepository.save(existing);
    }

    // ===================== DELETE =====================
    @Transactional
    public void deletePropertyLand(Long id) {
        PropertyLand propertyLand = propertyLandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property land not found with ID: " + id));
        propertyLandRepository.delete(propertyLand);
    }

    // ===================== DELETE BY PROPERTY ID =====================
    @Transactional
    public void deleteLandsByPropertyId(Long propertyId) {
        propertyLandRepository.deleteByPropertyId(propertyId);
    }
}