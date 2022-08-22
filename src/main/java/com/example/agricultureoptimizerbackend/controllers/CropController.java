package com.example.agricultureoptimizerbackend.controllers;

import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.Solution;
import com.example.agricultureoptimizerbackend.services.CropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/crop")
public class CropController {

    @Autowired
    CropService cropService;

    @GetMapping(value="/get-all")
    public ResponseEntity<List<Crop>> test(HttpServletResponse response){
        List<Crop> cropList = cropService.findAll();
        System.out.println("Crops");
        return ResponseEntity.ok(cropList);
    }

    @GetMapping(value="/get/{id}")
    public ResponseEntity<Crop> get(HttpServletResponse response, @PathVariable("id") Long id){

        Crop crop = cropService.findById(id);
        return ResponseEntity.ok(crop);
    }
}
