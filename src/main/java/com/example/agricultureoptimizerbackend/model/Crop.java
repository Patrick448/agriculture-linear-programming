package com.example.agricultureoptimizerbackend.model;

import com.example.agricultureoptimizerbackend.dto.CropDTO;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import javax.persistence.*;

@Entity(name="crop")
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", length = 20, nullable = false)
    private String name;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "cost", nullable = false)
    private Double cost;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Crop() {
    }

    public Crop(CropDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.cost = dto.getCost();
    }

    public Double getPrice() {
        return price;
    }

    public Crop(String name, Double price, Double cost) {
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

    // public int getAmount() {
   //     return amount;
   // }

    //public void setAmount(int amount) {
     //   this.amount = amount;
   // }
}
