package com.example.agricultureoptimizerbackend.model;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import javax.persistence.*;

@Entity(name="crop")
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "cost", nullable = false)
    private Double cost;
    @Column(name = "amount", nullable = false)
    private int amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Crop() {
    }

    public Double getPrice() {
        return price;
    }

    public Crop(Double price, Double cost, int amount) {
        this.price = price;
        this.cost = cost;
        this.amount = amount;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
