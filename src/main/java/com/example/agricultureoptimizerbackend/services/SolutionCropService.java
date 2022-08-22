package com.example.agricultureoptimizerbackend.services;

import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.Solution;
import com.example.agricultureoptimizerbackend.model.SolutionCrop;
import com.example.agricultureoptimizerbackend.repositories.CropRepository;
import com.example.agricultureoptimizerbackend.repositories.SolutionCropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolutionCropService {

    @Autowired
    private SolutionCropRepository repository;

    @Transactional(readOnly = true)
    public List<SolutionCrop> findAll(){
       List<SolutionCrop> result = repository.findAll();

       return result;
    }

    @Transactional
    public SolutionCrop save(SolutionCrop entity){
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public SolutionCrop findById(Long id){
        SolutionCrop result = repository.findById(id).orElse(null);
        return result;
    }
}
