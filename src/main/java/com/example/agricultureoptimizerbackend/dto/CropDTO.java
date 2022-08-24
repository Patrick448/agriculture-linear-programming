package com.example.agricultureoptimizerbackend.dto;

import com.example.agricultureoptimizerbackend.model.Crop;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class CropDTO {

    private long id;
    private String name;
    private double price;
    private double cost;
    private double space;
    private double time;

    public long getId() {
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
        this.space = entity.getSpace();
        this.time =entity.getTime();
    }

    public CropDTO() {
    }

    public double getPrice() {
        return price;
    }

    public CropDTO(String name, double price, double cost, double space, double time) {
        this.name = name;
        this.price = price;
        this.cost = cost;
        this.space = space;
        this.time = time;
        //this.amount = amount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
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

    public double getSpace() {
        return space;
    }

    public void setSpace(double space) {
        this.space = space;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
