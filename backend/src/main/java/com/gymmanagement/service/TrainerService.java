package com.gymmanagement.service;

import com.gymmanagement.dto.request.TrainerRequest;
import com.gymmanagement.dto.response.TrainerResponse;
import com.gymmanagement.entity.Member;
import com.gymmanagement.entity.Trainer;
import com.gymmanagement.enums.PlanStatus;
import com.gymmanagement.exception.ResourceNotFoundException;
import com.gymmanagement.repository.MemberRepository;
import com.gymmanagement.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private MemberRepository memberRepository;

    public List<TrainerResponse> getAllTrainers() {
        return trainerRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TrainerResponse> getActiveTrainers() {
        return trainerRepository.findByStatus(PlanStatus.ACTIVE).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TrainerResponse getTrainerById(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + id));
        return mapToResponse(trainer);
    }

    @Transactional
    public TrainerResponse createTrainer(TrainerRequest request) {
        Trainer trainer = Trainer.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .specialization(request.getSpecialization())
                .experience(request.getExperience())
                .joiningDate(request.getJoiningDate())
                .status(request.getStatus() != null ? request.getStatus() : PlanStatus.ACTIVE)
                .build();

        Trainer saved = trainerRepository.save(trainer);
        return mapToResponse(saved);
    }

    @Transactional
    public TrainerResponse updateTrainer(Long id, TrainerRequest request) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + id));

        trainer.setName(request.getName());
        trainer.setPhone(request.getPhone());
        trainer.setEmail(request.getEmail());
        trainer.setSpecialization(request.getSpecialization());
        trainer.setExperience(request.getExperience());
        if (request.getJoiningDate() != null) {
            trainer.setJoiningDate(request.getJoiningDate());
        }
        if (request.getStatus() != null) {
            trainer.setStatus(request.getStatus());
        }

        Trainer updated = trainerRepository.save(trainer);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteTrainer(Long id) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + id));
        trainerRepository.delete(trainer);
    }

    private TrainerResponse mapToResponse(Trainer trainer) {
        long memberCount = memberRepository.findAll().stream()
                .filter(m -> m.getTrainer() != null && m.getTrainer().getId().equals(trainer.getId()))
                .count();

        return TrainerResponse.builder()
                .id(trainer.getId())
                .name(trainer.getName())
                .phone(trainer.getPhone())
                .email(trainer.getEmail())
                .specialization(trainer.getSpecialization())
                .experience(trainer.getExperience())
                .joiningDate(trainer.getJoiningDate())
                .status(trainer.getStatus())
                .assignedMembersCount(memberCount)
                .createdAt(trainer.getCreatedAt())
                .build();
    }
}
