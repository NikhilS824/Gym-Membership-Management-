package com.gymmanagement.service;

import com.gymmanagement.dto.request.PlanRequest;
import com.gymmanagement.dto.response.PlanResponse;
import com.gymmanagement.entity.MembershipPlan;
import com.gymmanagement.enums.PlanStatus;
import com.gymmanagement.exception.BadRequestException;
import com.gymmanagement.exception.ResourceNotFoundException;
import com.gymmanagement.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public List<PlanResponse> getAllPlans() {
        return planRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PlanResponse> getActivePlans() {
        return planRepository.findByStatus(PlanStatus.ACTIVE).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PlanResponse getPlanById(Long id) {
        MembershipPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));
        return mapToResponse(plan);
    }

    @Transactional
    public PlanResponse createPlan(PlanRequest request) {
        validatePlanRequest(request);

        if (planRepository.existsByName(request.getName())) {
            throw new BadRequestException("Plan with name '" + request.getName() + "' already exists");
        }

        MembershipPlan plan = MembershipPlan.builder()
                .name(request.getName())
                .durationValue(request.getDurationValue())
                .durationUnit(request.getDurationUnit())
                .price(request.getPrice())
                .description(request.getDescription())
                .benefits(request.getBenefits())
                .status(request.getStatus() != null ? request.getStatus() : PlanStatus.ACTIVE)
                .build();

        MembershipPlan saved = planRepository.save(plan);
        return mapToResponse(saved);
    }

    @Transactional
    public PlanResponse updatePlan(Long id, PlanRequest request) {
        validatePlanRequest(request);

        MembershipPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));

        if (!plan.getName().equalsIgnoreCase(request.getName()) && planRepository.existsByName(request.getName())) {
            throw new BadRequestException("Plan with name '" + request.getName() + "' already exists");
        }

        plan.setName(request.getName());
        plan.setDurationValue(request.getDurationValue());
        plan.setDurationUnit(request.getDurationUnit());
        plan.setPrice(request.getPrice());
        plan.setDescription(request.getDescription());
        plan.setBenefits(request.getBenefits());
        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        }

        MembershipPlan updated = planRepository.save(plan);
        return mapToResponse(updated);
    }

    @Transactional
    public void deletePlan(Long id) {
        MembershipPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found with id: " + id));
        plan.setStatus(PlanStatus.INACTIVE);
        planRepository.save(plan);
    }

    private void validatePlanRequest(PlanRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Plan name cannot be empty");
        }
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Plan price cannot be negative");
        }
        if (request.getDurationValue() == null || request.getDurationValue() <= 0) {
            throw new BadRequestException("Plan duration must be greater than zero");
        }
    }

    public PlanResponse mapToResponse(MembershipPlan plan) {
        return PlanResponse.builder()
                .id(plan.getId())
                .name(plan.getName())
                .durationValue(plan.getDurationValue())
                .durationUnit(plan.getDurationUnit())
                .price(plan.getPrice())
                .description(plan.getDescription())
                .benefits(plan.getBenefits())
                .status(plan.getStatus())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }
}
