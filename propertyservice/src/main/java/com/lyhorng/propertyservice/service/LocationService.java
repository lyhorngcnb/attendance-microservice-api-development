package com.lyhorng.propertyservice.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lyhorng.propertyservice.model.Commune;
import com.lyhorng.propertyservice.model.District;
import com.lyhorng.propertyservice.model.Province;
import com.lyhorng.propertyservice.model.Village;
import com.lyhorng.propertyservice.repository.CommuneRepository;
import com.lyhorng.propertyservice.repository.DistrictRepository;
import com.lyhorng.propertyservice.repository.ProvinceRepository;
import com.lyhorng.propertyservice.repository.VillageRepository;

@Service
public class LocationService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private CommuneRepository communeRepository;

    @Autowired
    private VillageRepository villageRepository;

    // ===================== PROVINCE =====================
    @Transactional(readOnly = true)
    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Province getProvinceById(Long id) {
        return provinceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Province not found with ID: " + id));
    }

    // ===================== DISTRICT =====================
    @Transactional(readOnly = true)
    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<District> getDistrictsByProvinceId(Long provinceId) {
        return districtRepository.findByProvinceId(provinceId);
    }
    
    @Transactional(readOnly = true)
    public District getDistrictById(Long id) {
        return districtRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("District not found with ID: " + id));
    }

    // ===================== COMMUNE =====================
    @Transactional(readOnly = true)
    public List<Commune> getAllCommunes() {
        return communeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Commune> getCommunesByDistrictId(Long districtId) {
        return communeRepository.findByDistrictId(districtId);
    }
    
    @Transactional(readOnly = true)
    public Commune getCommuneById(Long id) {
        return communeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commune not found with ID: " + id));
    }

    // ===================== VILLAGE =====================
    @Transactional(readOnly = true)
    public List<Village> getAllVillages() {
        return villageRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Village> getVillagesByCommuneId(Long communeId) {
        return villageRepository.findByCommuneId(communeId);
    }
    
    @Transactional(readOnly = true)
    public Village getVillageById(Long id) {
        return villageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Village not found with ID: " + id));
    }
}