package com.example.agricultureoptimizerbackend.dto;

import com.example.agricultureoptimizerbackend.model.Crop;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class CropDTO {

    private Long id;
    private String name;
    private Double price;
    private Double cost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CropDTO(Crop entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.cost = entity.getCost();
    }

    public CropDTO() {
    }

    public Double getPrice() {
        return price;
    }

    public CropDTO(String name, Double price, Double cost) {
        this.name = name;
        this.price = price;
        this.cost = cost;
        //this.amount = amount;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProfit(){
        return this.price - this.cost;
    }


}
