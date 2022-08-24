package com.example.agricultureoptimizerbackend.dto;

import com.example.agricultureoptimizerbackend.model.InputData;
import com.example.agricultureoptimizerbackend.model.Solution;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

public class InputDataDTO implements Serializable {
    private static final long serialVersionUID=1L;

    private Long id;
    private double budget;
    private double space;
    @JsonIgnore
    private SolutionDTO solution;

    public InputDataDTO(InputData entity) {
        this.id = entity.getId();
        this.budget = entity.getBudget();
        this.space = entity.getSpace();

    }

    public InputDataDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getSpace() {
        return space;
    }

    public void setSpace(double space) {
        this.space = space;
    }

    public SolutionDTO getSolution() {
        return solution;
    }

    public void setSolution(SolutionDTO solution) {
        this.solution = solution;
    }
}
