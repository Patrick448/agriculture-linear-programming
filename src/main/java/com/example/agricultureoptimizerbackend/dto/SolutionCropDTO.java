package com.example.agricultureoptimizerbackend.dto;

import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.Field;
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
    private double price;
    private double space;
    private double time;
    private double cost;
    private FieldDTO field;
    private int timeFrame;

    public SolutionCropDTO() {
    }

    public SolutionCropDTO(SolutionCrop entity) {
        this.crop = new CropDTO(entity.getCrop());
        this.amount = entity.getAmount();
        this.id = entity.getId();
        this.price = entity.getPrice();
        this.space = entity.getSpace();
        this.time = entity.getTime();
        this.cost = entity.getCost();
        this.field = new FieldDTO(entity.getField());
        this.timeFrame = entity.getTimeFrame();
    }

    public SolutionCropDTO(Long id, int amount, CropDTO crop, double price, double space, double time, double cost, int timeFrame, FieldDTO field) {
        this.id = id;
        this.amount = amount;
        this.crop = crop;
        this.price = price;
        this.space = space;
        this.time = time;
        this.cost = cost;
        this.timeFrame = timeFrame;
        this.field = field;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSpace() {
        return space;
    }

    public void setSpace(double space) {
        this.space = space;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public FieldDTO getField() {
        return field;
    }

    public void setField(FieldDTO field) {
        this.field = field;
    }

    public int getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(int timeFrame) {
        this.timeFrame = timeFrame;
    }
}
