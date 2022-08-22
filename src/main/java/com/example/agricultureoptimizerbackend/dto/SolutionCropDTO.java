package com.example.agricultureoptimizerbackend.dto;

import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.Solution;
import com.example.agricultureoptimizerbackend.model.SolutionCrop;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

public class SolutionCropDTO implements Serializable {
    private static final long serialVersionUID=1L;

    private Long id;
    private int amount;
    private CropDTO crop;

    public SolutionCropDTO() {
    }

    public SolutionCropDTO(SolutionCrop entity) {
        this.crop = new CropDTO(entity.getCrop());
        this.amount = entity.getAmount();
        this.id = entity.getId();
    }

    public SolutionCropDTO(int amount, CropDTO crop) {
        this.amount = amount;
        this.crop = crop;
    }

    public CropDTO getCrop() {
        return crop;
    }

    public void setCrop(CropDTO crop) {
        this.crop = crop;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


}
