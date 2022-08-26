package com.example.agricultureoptimizerbackend.model;

import com.example.agricultureoptimizerbackend.dto.FieldDTO;

import javax.persistence.*;

@Entity
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="size")
    private double size;

    @Column(name="name", length = 20)
    private String name;

    public Field(Long id, double size, String name) {
        this.id = id;
        this.size = size;
        this.name = name;
    }

    public Field(FieldDTO dto) {
        this.id = dto.getId();
        this.size = dto.getSize();
        this.name = dto.getName();
    }

    public Field() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
