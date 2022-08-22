package com.example.agricultureoptimizerbackend.repositories;

import com.example.agricultureoptimizerbackend.model.Crop;
import com.example.agricultureoptimizerbackend.model.SolutionCrop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionCropRepository extends JpaRepository<SolutionCrop, Long> {
}
