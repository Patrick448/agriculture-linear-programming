package com.example.agricultureoptimizerbackend.repositories;

import com.example.agricultureoptimizerbackend.model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Crop, Long> {
}
