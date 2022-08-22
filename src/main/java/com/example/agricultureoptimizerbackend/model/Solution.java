package com.example.agricultureoptimizerbackend.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToMany
    private List<SolutionCrop> solutionCrops;
    @OneToOne
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
