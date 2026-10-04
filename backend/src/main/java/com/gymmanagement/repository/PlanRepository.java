package com.gymmanagement.repository;

import com.gymmanagement.entity.MembershipPlan;
import com.gymmanagement.enums.PlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<MembershipPlan, Long> {
    List<MembershipPlan> findByStatus(PlanStatus status);
    Boolean existsByName(String name);
}
