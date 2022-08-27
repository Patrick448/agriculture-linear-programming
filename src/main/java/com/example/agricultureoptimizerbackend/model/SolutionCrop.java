package com.example.agricultureoptimizerbackend.model;

import com.example.agricultureoptimizerbackend.dto.CropDTO;
import com.example.agricultureoptimizerbackend.dto.SolutionCropDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class SolutionCrop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "amount", nullable = false)
    private int amount;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "space", nullable = false)
    private double space;
    @Column(name = "time", nullable = false)
    private double time;
    @Column(name = "cost", nullable = false)
    private double cost;
    @Column(name="time_frame")
    private int timeFrame;

    @JsonIgnore
    @ManyToOne
    private Solution solution;

    @ManyToOne(cascade={CascadeType.MERGE})
    private Crop crop;

    @ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST})
    private Field field;



    public SolutionCrop() {
    }

    public SolutionCrop(Long id, int amount, double price, double space, double time, double cost, int timeFrame, Solution solution, Crop crop, Field field) {
        this.id = id;
        this.amount = amount;
        this.price = price;
        this.space = space;
        this.time = time;
        this.cost = cost;
        this.timeFrame = timeFrame;
        this.solution = solution;
        this.crop = crop;
        this.field = field;
    }

    public SolutionCrop(SolutionCropDTO dto) {
        this.crop = new Crop(dto.getCrop());
        this.amount = dto.getAmount();
        this.id = dto.getId();
        this.price = dto.getPrice();
        this.space = dto.getSpace();
        this.time = dto.getTime();
        this.cost = dto.getCost();
        this.timeFrame= dto.getTimeFrame();
        this.field = new Field(dto.getField());
    }

    public Crop getCrop() {
        return crop;
    }

    public void setCrop(Crop crop) {
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

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
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

    public int getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(int timeFrame) {
        this.timeFrame = timeFrame;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
