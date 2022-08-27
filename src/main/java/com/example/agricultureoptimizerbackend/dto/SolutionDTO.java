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
    private List<FieldDTO> fields;
    //private int timeFrames;


    public SolutionDTO(){

    }

    public SolutionDTO(Long id, List<SolutionCropDTO> solutionCrops, InputDataDTO inputData) {
        this.id = id;
        this.solutionCrops = solutionCrops;
        this.inputData = inputData;
       // this.timeFrames = timeFrames;
    }

    public SolutionDTO(Solution entity) {
        this.id = entity.getId();
        this.solutionCrops = entity.getSolutionCrops().stream().map(SolutionCropDTO::new).collect(Collectors.toList());
        this.fields = entity.getFields().stream().map(FieldDTO::new).collect(Collectors.toList());

        //this.timeFrames = entity.getTimeFrames();
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

    public List<FieldDTO> getFields() {
        return fields;
    }

    public void setFields(List<FieldDTO> fields) {
        this.fields = fields;
    }
}
