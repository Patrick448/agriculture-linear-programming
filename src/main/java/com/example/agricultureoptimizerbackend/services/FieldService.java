package com.example.agricultureoptimizerbackend.services;

import com.example.agricultureoptimizerbackend.dto.FieldDTO;
import com.example.agricultureoptimizerbackend.model.Field;
import com.example.agricultureoptimizerbackend.repositories.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldService {

    @Autowired
    private FieldRepository repository;

    @Transactional(readOnly = true)
    public List<FieldDTO> findAll() {
        List<Field> result = repository.findAll();

        return result.stream().map(FieldDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Field> findAllEntities() {
        List<Field> result = repository.findAll();

        return result;
    }

    @Transactional(readOnly = true)
    public FieldDTO findById(Long id) {
        Field result = repository.findById(id).orElse(null);
        if (result == null) {
            return null;
        }
        return new FieldDTO(result);
    }

    @Transactional
    public Field save(Field entity) {
        return repository.save(entity);
    }

    @Transactional
    public boolean save(List<Field> entityList) {

        for(Field entity:entityList){
            repository.save(entity);
        }
        return true;
    }

    @Transactional
    public Field save(FieldDTO dto) {
        return repository.save(new Field(dto));
    }


}
