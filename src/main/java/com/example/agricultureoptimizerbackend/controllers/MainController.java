package com.example.agricultureoptimizerbackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/main")
public class MainController {
    @GetMapping(value="/test")
    public ResponseEntity<String> test(HttpServletResponse response){
        System.out.println("Test");
        return ResponseEntity.ok("Test");
    }
}