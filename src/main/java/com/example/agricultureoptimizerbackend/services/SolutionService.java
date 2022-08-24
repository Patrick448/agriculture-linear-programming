package com.example.agricultureoptimizerbackend.services;

import com.example.agricultureoptimizerbackend.dto.CropDTO;
import com.example.agricultureoptimizerbackend.dto.SolutionDTO;
import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.Solution;
import com.example.agricultureoptimizerbackend.repositories.CropRepository;
import com.example.agricultureoptimizerbackend.repositories.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolutionService {

    @Autowired
    private SolutionRepository repository;

    @Transactional(readOnly = true)
    public List<SolutionDTO> findAll(){
       List<Solution> result = repository.findAll();

       return result.stream().map(SolutionDTO::new).collect(Collectors.toList());    }

    @Transactional
    public Solution save(Solution entity){
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public Solution findById(Long id){
        Solution result = repository.findById(id).orElse(null);
        return result;
    }

    @Transactional(readOnly = true)
    public SolutionDTO findByIdDTO(Long id){
        Solution result = repository.findById(id).orElse(null);

        if(result != null)
            return new SolutionDTO(result);

        return null;
    }

    @Transactional
    public Solution save(SolutionDTO dto){
        return repository.save(new Solution(dto));
    }
}
