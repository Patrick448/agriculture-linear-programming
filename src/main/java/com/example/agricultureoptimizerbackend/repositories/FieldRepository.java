package com.example.agricultureoptimizerbackend.repositories;

import com.example.agricultureoptimizerbackend.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<Field, Long> {
}