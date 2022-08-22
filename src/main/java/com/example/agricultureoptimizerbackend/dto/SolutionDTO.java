package com.example.agricultureoptimizerbackend.dto;

import com.example.agricultureoptimizerbackend.model.InputData;
import com.example.agricultureoptimizerbackend.model.Solution;
import com.example.agricultureoptimizerbackend.model.SolutionCrop;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class SolutionDTO implements Serializable {
    private static final long serialVersionUID=1L;

    private Long id;
    private List<SolutionCropDTO> solutionCrops;
    private InputDataDTO inputData;


    public SolutionDTO(){

    }


    public SolutionDTO(Solution entity) {
        this.id = entity.getId();
        this.solutionCrops = entity.getSolutionCrops().stream().map(SolutionCropDTO::new).collect(Collectors.toList());

        if(this.inputData == null)
            this.inputData = new InputDataDTO(entity.getInputData());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<SolutionCropDTO> getSolutionCrops() {
        return solutionCrops;
    }

    public void setSolutionCrops(List<SolutionCropDTO> solutionCrops) {
        this.solutionCrops = solutionCrops;
    }

    public InputDataDTO getInputData() {
        return inputData;
    }

    public void setInputData(InputDataDTO inputData) {
        this.inputData = inputData;
    }

}
