package com.example.agricultureoptimizerbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class SolutionCrop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "amount", nullable = false)
    private int amount;
    @JsonIgnore
    @ManyToOne
    private Solution solution;

    @ManyToOne
    private Crop crop;

    public SolutionCrop() {
    }

    public SolutionCrop(int amount, Solution solution, Crop crop) {
        this.amount = amount;
        this.solution = solution;
        this.crop = crop;
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
}
