package com.gymmanagement.dto.response;

import com.gymmanagement.enums.PlanStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TrainerResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String specialization;
    private Integer experience;
    private LocalDate joiningDate;
    private PlanStatus status;
    private long assignedMembersCount;
    private LocalDateTime createdAt;

    public TrainerResponse() {}

    public TrainerResponse(Long id, String name, String phone, String email, String specialization, Integer experience, LocalDate joiningDate, PlanStatus status, long assignedMembersCount, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.specialization = specialization;
        this.experience = experience;
        this.joiningDate = joiningDate;
        this.status = status;
        this.assignedMembersCount = assignedMembersCount;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public PlanStatus getStatus() { return status; }
    public void setStatus(PlanStatus status) { this.status = status; }

    public long getAssignedMembersCount() { return assignedMembersCount; }
    public void setAssignedMembersCount(long assignedMembersCount) { this.assignedMembersCount = assignedMembersCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private String phone;
        private String email;
        private String specialization;
        private Integer experience;
        private LocalDate joiningDate;
        private PlanStatus status;
        private long assignedMembersCount;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder specialization(String specialization) { this.specialization = specialization; return this; }
        public Builder experience(Integer experience) { this.experience = experience; return this; }
        public Builder joiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; return this; }
        public Builder status(PlanStatus status) { this.status = status; return this; }
        public Builder assignedMembersCount(long assignedMembersCount) { this.assignedMembersCount = assignedMembersCount; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public TrainerResponse build() {
            return new TrainerResponse(id, name, phone, email, specialization, experience, joiningDate, status, assignedMembersCount, createdAt);
        }
    }
}
