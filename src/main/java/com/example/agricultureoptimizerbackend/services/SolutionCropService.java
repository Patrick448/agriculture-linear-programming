package com.example.agricultureoptimizerbackend.services;

import com.example.agricultureoptimizerbackend.dto.SolutionCropDTO;
import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.Solution;
import com.example.agricultureoptimizerbackend.model.SolutionCrop;
import com.example.agricultureoptimizerbackend.repositories.CropRepository;
import com.example.agricultureoptimizerbackend.repositories.SolutionCropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolutionCropService {

    @Autowired
    private SolutionCropRepository repository;

    @Transactional(readOnly = true)
    public List<SolutionCropDTO> findAll(){
       List<SolutionCrop> result = repository.findAll();

       return result.stream().map(SolutionCropDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public SolutionCrop save(SolutionCrop entity){
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public SolutionCropDTO findById(Long id){
        SolutionCrop result = repository.findById(id).orElse(null);
        if(result == null){
            return null;
        }
        return new SolutionCropDTO(result);
    }

    @Transactional
    public SolutionCropDTO save(SolutionCropDTO dto){
        return new SolutionCropDTO(repository.save(new SolutionCrop(dto)));
    }
}
