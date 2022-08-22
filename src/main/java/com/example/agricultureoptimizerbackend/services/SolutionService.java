package com.example.agricultureoptimizerbackend.services;

import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.Solution;
import com.example.agricultureoptimizerbackend.repositories.CropRepository;
import com.example.agricultureoptimizerbackend.repositories.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolutionService {

    @Autowired
    private SolutionRepository repository;

    @Transactional(readOnly = true)
    public List<Solution> findAll(){
       List<Solution> result = repository.findAll();

       return result;
    }

    @Transactional
    public Solution save(Solution entity){
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public Solution findById(Long id){
        Solution result = repository.findById(id).orElse(null);
        return result;
    }
}
