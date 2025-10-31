package com.lyhorng.propertyservice.service;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lyhorng.propertyservice.model.PropertyInfo;
import com.lyhorng.propertyservice.repository.PropertyInfoRepository;

@Service
public class PropertyInfoService {

    @Autowired
    private PropertyInfoRepository propertyInfoRepository;

    public List<PropertyInfo> getAllPropertyInfo() {
        return propertyInfoRepository.findAll();
    }

    // Create Property Info
    public PropertyInfo createPropertyInfo(String property_info) {
        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.setPropertyInfo(property_info);

        return propertyInfoRepository.save(propertyInfo);
    }

    public PropertyInfo updatePropertyInfo(Long id, String property_info) {
        Optional<PropertyInfo> existPropertyInfo = propertyInfoRepository.findById(id);
        PropertyInfo propertyInfo = new PropertyInfo();
        if (existPropertyInfo.isPresent()) {
            propertyInfo = existPropertyInfo.get();
        }
        return propertyInfoRepository.save(propertyInfo);
    }

    public void deletePropertyInfo(Long id) {
        Optional<PropertyInfo> propertyInfo = propertyInfoRepository.findById(id);

        if (propertyInfo.isPresent()) {
            propertyInfoRepository.delete(propertyInfo.get());
        } else {
            throw new RuntimeException("Property Info not found with ID: " + id);
        }
    }

    public PropertyInfo getPropertyInfo(Long id) {
        return propertyInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProperInfo Notfound " + id));
    }
}
