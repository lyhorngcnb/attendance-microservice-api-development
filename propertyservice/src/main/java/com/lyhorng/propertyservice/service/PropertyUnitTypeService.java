package com.lyhorng.propertyservice.service;

import com.lyhorng.propertyservice.model.PropertyUnitType;
import com.lyhorng.propertyservice.repository.PropertyUnitTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PropertyUnitTypeService {

    @Autowired
    private PropertyUnitTypeRepository propertyUnitTypeRepository;

    // ===================== LIST ALL =====================
    @Transactional(readOnly = true)
    public List<PropertyUnitType> getAllUnitTypes() {
        return propertyUnitTypeRepository.findAll();
    }

    // ===================== LIST ACTIVE =====================
    @Transactional(readOnly = true)
    public List<PropertyUnitType> getActiveUnitTypes() {
        return propertyUnitTypeRepository.findByIsActiveTrue();
    }

    // ===================== GET BY ID =====================
    @Transactional(readOnly = true)
    public PropertyUnitType getUnitTypeById(Long id) {
        return propertyUnitTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property unit type not found with ID: " + id));
    }

    // ===================== CREATE =====================
    @Transactional
    public PropertyUnitType createUnitType(
            String name,
            String code,
            String description,
            BigDecimal conversionToSqm,
            Boolean isActive) {
        
        PropertyUnitType unitType = new PropertyUnitType();
        unitType.setName(name);
        unitType.setCode(code);
        unitType.setDescription(description);
        unitType.setConversionToSqm(conversionToSqm);
        unitType.setIsActive(isActive != null ? isActive : true);
        unitType.setCreatedAt(LocalDateTime.now());
        unitType.setUpdatedAt(LocalDateTime.now());

        return propertyUnitTypeRepository.save(unitType);
    }

    // ===================== UPDATE =====================
    @Transactional
    public PropertyUnitType updateUnitType(
            Long id,
            String name,
            String code,
            String description,
            BigDecimal conversionToSqm,
            Boolean isActive) {
        
        PropertyUnitType existing = propertyUnitTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property unit type not found with ID: " + id));

        if (name != null) existing.setName(name);
        if (code != null) existing.setCode(code);
        if (description != null) existing.setDescription(description);
        if (conversionToSqm != null) existing.setConversionToSqm(conversionToSqm);
        if (isActive != null) existing.setIsActive(isActive);
        
        existing.setUpdatedAt(LocalDateTime.now());
        return propertyUnitTypeRepository.save(existing);
    }

    // ===================== DELETE =====================
    @Transactional
    public void deleteUnitType(Long id) {
        PropertyUnitType unitType = propertyUnitTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property unit type not found with ID: " + id));
        propertyUnitTypeRepository.delete(unitType);
    }
}