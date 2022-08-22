package com.example.agricultureoptimizerbackend.services;

import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.repositories.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CropService {

    @Autowired
    private CropRepository repository;

    @Transactional(readOnly = true)
    public List<Crop> findAll(){
       List<Crop> result = repository.findAll();

       return result;
    }

    @Transactional(readOnly = true)
    public Crop findById(Long id){
        Crop result = repository.findById(id).orElse(null);
        return result;
    }

    @Transactional
    public Crop save(Crop entity){
        return repository.save(entity);
    }
}
