package com.example.agricultureoptimizerbackend.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class InputData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "budget", nullable = false)
    private double budget;
    //private List<Double> plots;
    //private List<Crop> crops;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InputData() {
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }


}
