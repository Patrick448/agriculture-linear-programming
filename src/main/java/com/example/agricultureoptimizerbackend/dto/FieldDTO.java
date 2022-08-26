package com.example.agricultureoptimizerbackend.dto;

import com.example.agricultureoptimizerbackend.model.Field;

import java.io.Serializable;
import java.util.Objects;

public class FieldDTO implements Serializable {
    private final Long id;
    private final double size;
    private final String name;

    public FieldDTO(Long id, double size, String name) {
        this.id = id;
        this.size = size;
        this.name = name;
    }
    public FieldDTO(Field entity) {
        this.id = entity.getId();
        this.size = entity.getSize();
        this.name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public double getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDTO entity = (FieldDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.size, entity.size) &&
                Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, size, name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "size = " + size + ", " +
                "name = " + name + ")";
    }
}
