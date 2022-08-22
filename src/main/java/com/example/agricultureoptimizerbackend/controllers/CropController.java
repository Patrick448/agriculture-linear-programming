package com.example.agricultureoptimizerbackend.controllers;

import com.example.agricultureoptimizerbackend.dto.CropDTO;
import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.Solution;
import com.example.agricultureoptimizerbackend.services.CropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/crop")
public class CropController {

    @Autowired
    CropService cropService;

    @GetMapping(value="/get-all")
    public ResponseEntity<List<CropDTO>> test(HttpServletResponse response){
        List<CropDTO> cropList = cropService.findAll();
        System.out.println("Crops");
        return ResponseEntity.ok(cropList);
    }

    @GetMapping(value="/get/{id}")
    public ResponseEntity<CropDTO> get(HttpServletResponse response, @PathVariable("id") Long id){

        CropDTO crop = cropService.findById(id);
        return ResponseEntity.ok(crop);
    }

    @PutMapping (value="/new")
    public ResponseEntity<CropDTO> get(HttpServletResponse response, @RequestBody Crop crop){
        return ResponseEntity.ok(new CropDTO(cropService.save(crop)));
    }
}
