package com.lyhorng.propertyservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lyhorng.propertyservice.model.Agency;
import com.lyhorng.propertyservice.repository.AgencyRepository;

@Service
public class AgencyService {

    @Autowired
    public AgencyRepository agencyRepository;

    public List<Agency> getAllAgency() {
        return agencyRepository.findAll();
    }

    // Create a new
    public Agency createAgency(String agencyName) {
        Agency agencies = new Agency();
        agencies.setAgencyName(agencyName);
        return agencyRepository.save(agencies);
    }

    // Update a existing
    public Agency updateAgency(Long id, String agencyName) {
        Optional<Agency> existingAgency = agencyRepository.findById(id);
        if (existingAgency.isPresent()) {
            Agency agency = existingAgency.get();
            agency.setAgencyName(agencyName);
            return agencyRepository.save(agency);
        } else {
            throw new RuntimeException("Agency not found ID :" + id);
        }
    }

    // Delete

    // get Info
    public Agency getAgency(Long id){
        return agencyRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Agency Not found ID: "+ id));
    }
}
