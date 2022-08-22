package com.example.agricultureoptimizerbackend.services;

import com.example.agricultureoptimizerbackend.dto.CropDTO;
import com.example.agricultureoptimizerbackend.dto.InputDataDTO;
import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.InputData;
import com.example.agricultureoptimizerbackend.repositories.CropRepository;
import com.example.agricultureoptimizerbackend.repositories.InputDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InputDataService {

    @Autowired
    private InputDataRepository repository;

    @Transactional(readOnly = true)
    public List<InputDataDTO> findAll(){
       List<InputData> result = repository.findAll();

       return result.stream().map(InputDataDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InputDataDTO findById(Long id){
        InputData result = repository.findById(id).orElse(null);
        if(result == null){
            return null;
        }
        return new InputDataDTO(result);
    }

    @Transactional
    public InputData save(InputData entity){
        return repository.save(entity);
    }

    @Transactional
    public InputData save(InputDataDTO dto){
        return repository.save(new InputData(dto));
    }
}
