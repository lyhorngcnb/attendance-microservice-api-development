package com.lyhorng.propertyservice.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lyhorng.propertyservice.model.PropertyUnitType;
import com.lyhorng.propertyservice.repository.PropertyUnitTypeRepository;

@Service
public class PropertyUnitTypeService {
    
    @Autowired
    private PropertyUnitTypeRepository propertyUnitTypeRepository;

    // List
    public List<PropertyUnitType> getAllPropertyUnitType() {
        return propertyUnitTypeRepository.findAll();
    }
    // Create
    public PropertyUnitType createPropertyUnitType(String unitType){
        PropertyUnitType propertyUnitType = new PropertyUnitType();
        propertyUnitType.setPropertyUnitType(unitType);

        return propertyUnitTypeRepository.save(propertyUnitType);
    }
    // Update
    public PropertyUnitType updatePerPropertyUnitType(Long id, String unitType){
        Optional<PropertyUnitType> existingPropertyUnitType = propertyUnitTypeRepository.findById(id);
        if(existingPropertyUnitType.isPresent()) {
            PropertyUnitType propertyUnitType = existingPropertyUnitType.get();
            propertyUnitType.setPropertyUnitType(unitType);

             return propertyUnitTypeRepository.save(propertyUnitType);
        } else {
              throw new RuntimeException("Property  Type not found with ID: " + id);
        }
    }
    // Delete
    public void deletePropertyUnitType(Long id) {
        Optional<PropertyUnitType> propertyUnitType = propertyUnitTypeRepository.findById(id);
        if(propertyUnitType.isPresent()) {
            propertyUnitTypeRepository.delete(propertyUnitType.get());
        } else {
            throw new RuntimeException("Property Type not Found with ID: " + id);
        }
    }
    //View
    public PropertyUnitType getPropertyUnitType(Long id) {
        return propertyUnitTypeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Property Unit Type not found with ID: " + id));
    }
}
