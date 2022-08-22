package com.example.agricultureoptimizerbackend.model;

import com.example.agricultureoptimizerbackend.dto.InputDataDTO;
import com.example.agricultureoptimizerbackend.dto.SolutionCropDTO;
import com.example.agricultureoptimizerbackend.dto.SolutionDTO;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToMany(mappedBy = "solution",cascade=CascadeType.PERSIST)
    private List<SolutionCrop> solutionCrops;

    @OneToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "input_data_id")
    private InputData inputData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Solution() {
    }

    public Solution(SolutionDTO dto) {
        this.id = dto.getId();
        this.solutionCrops = dto.getSolutionCrops().stream().map(SolutionCrop::new).collect(Collectors.toList());
        InputData inputData = new InputData(dto.getInputData());
        inputData.setSolution(this);
        this.inputData = inputData;
    }

    public List<SolutionCrop> getSolutionCrops() {
        return solutionCrops;
    }

    public void setSolutionCrops(List<SolutionCrop> solutionCrops) {
        this.solutionCrops = solutionCrops;
    }

    public InputData getInputData() {
        return inputData;
    }

    public void setInputData(InputData inputData) {
        this.inputData = inputData;
    }
}
