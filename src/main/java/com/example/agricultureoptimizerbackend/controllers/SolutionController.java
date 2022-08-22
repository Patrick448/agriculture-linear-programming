package com.example.agricultureoptimizerbackend.controllers;

import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.Solution;
import com.example.agricultureoptimizerbackend.services.CropService;
import com.example.agricultureoptimizerbackend.services.SolutionService;
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
@RequestMapping("/solution")
public class SolutionController {

    @Autowired
    SolutionService solutionService;

    @GetMapping(value="/get-all")
    public ResponseEntity<List<Solution>> test(HttpServletResponse response){
        List<Solution> cropList = solutionService.findAll();
        System.out.println("Solutions");
        return ResponseEntity.ok(cropList);
    }

    @GetMapping(value="/get/{id}")
    public ResponseEntity<Solution> get(HttpServletResponse response, @PathVariable("id") Long id){

        Solution solution= solutionService.findById(id);
        return ResponseEntity.ok(solution);
    }
}
