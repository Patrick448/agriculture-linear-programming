package com.example.agricultureoptimizerbackend.controllers;

import com.example.agricultureoptimizerbackend.dto.CropDTO;
import com.example.agricultureoptimizerbackend.dto.FieldDTO;
import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.Field;
import com.example.agricultureoptimizerbackend.services.CropService;
import com.example.agricultureoptimizerbackend.services.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/field")
public class FieldController {

    @Autowired
    FieldService fieldService;

    @GetMapping(value="/get-all")
    public ResponseEntity<List<FieldDTO>> getAll(HttpServletResponse response){
        List<FieldDTO> fieldList = fieldService.findAll();
        System.out.println("Crops");
        return ResponseEntity.ok(fieldList);
    }

    @GetMapping(value="/get/{id}")
    public ResponseEntity<FieldDTO> get(HttpServletResponse response, @PathVariable("id") Long id){

        FieldDTO field = fieldService.findById(id);
        return ResponseEntity.ok(field);
    }

    @PutMapping (value="/new")
    public ResponseEntity<FieldDTO> newCrop(HttpServletResponse response, @RequestBody Field field){
        return ResponseEntity.ok(new FieldDTO(fieldService.save(field)));
    }

    @PutMapping (value="/new-multiple")
    public ResponseEntity<List<FieldDTO>> newCrop(HttpServletResponse response, @RequestBody List<Field> fields){
        List<FieldDTO> dtos = new ArrayList<FieldDTO>();
        for(Field field: fields){
            dtos.add(new FieldDTO(fieldService.save(field)));
        }
        return ResponseEntity.ok(dtos);
    }
}
