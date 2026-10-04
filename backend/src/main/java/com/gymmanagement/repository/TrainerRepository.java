package com.gymmanagement.repository;

import com.gymmanagement.entity.Trainer;
import com.gymmanagement.enums.PlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    List<Trainer> findByStatus(PlanStatus status);
}
