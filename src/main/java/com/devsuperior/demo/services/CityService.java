package com.devsuperior.demo.services;

import com.devsuperior.demo.repositories.CityRepository;
import com.devsuperior.demo.services.exceptions.DatabaseException;
import com.devsuperior.demo.services.exceptions.ResourceNotFoundException;
import com.devsuperior.demo.dto.CityDTO;
import com.devsuperior.demo.entities.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {
    @Autowired
    private CityRepository repository;
    @Transactional(readOnly = true)
    public List<CityDTO> findAll() {
        List<City> cityList = repository.findAll(Sort.by("name"));
        return cityList.stream().map(x -> new CityDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    public CityDTO insert(CityDTO cityDTO) {
        City Entity = new City();
        Entity.setName(cityDTO.getName());
        return new CityDTO(repository.save(Entity));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Id City not found " + id);
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
