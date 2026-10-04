package com.gymmanagement.dto.response;

import com.gymmanagement.enums.MemberStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberResponse {
    private Long id;
    private String memberId;
    private String fullName;
    private String phone;
    private String email;
    private LocalDate dateOfBirth;
    private Integer age;
    private String gender;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private LocalDate joiningDate;
    private String profilePhoto;
    private MemberStatus status;
    private Long trainerId;
    private String trainerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MemberResponse() {}

    public MemberResponse(Long id, String memberId, String fullName, String phone, String email, LocalDate dateOfBirth, Integer age, String gender, String address, String emergencyContactName, String emergencyContactPhone, LocalDate joiningDate, String profilePhoto, MemberStatus status, Long trainerId, String trainerName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.joiningDate = joiningDate;
        this.profilePhoto = profilePhoto;
        this.status = status;
        this.trainerId = trainerId;
        this.trainerName = trainerName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }

    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public String getProfilePhoto() { return profilePhoto; }
    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }

    public MemberStatus getStatus() { return status; }
    public void setStatus(MemberStatus status) { this.status = status; }

    public Long getTrainerId() { return trainerId; }
    public void setTrainerId(Long trainerId) { this.trainerId = trainerId; }

    public String getTrainerName() { return trainerName; }
    public void setTrainerName(String trainerName) { this.trainerName = trainerName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String memberId;
        private String fullName;
        private String phone;
        private String email;
        private LocalDate dateOfBirth;
        private Integer age;
        private String gender;
        private String address;
        private String emergencyContactName;
        private String emergencyContactPhone;
        private LocalDate joiningDate;
        private String profilePhoto;
        private MemberStatus status;
        private Long trainerId;
        private String trainerName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder memberId(String memberId) { this.memberId = memberId; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder dateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
        public Builder age(Integer age) { this.age = age; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder emergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; return this; }
        public Builder emergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; return this; }
        public Builder joiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; return this; }
        public Builder profilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; return this; }
        public Builder status(MemberStatus status) { this.status = status; return this; }
        public Builder trainerId(Long trainerId) { this.trainerId = trainerId; return this; }
        public Builder trainerName(String trainerName) { this.trainerName = trainerName; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public MemberResponse build() {
            return new MemberResponse(id, memberId, fullName, phone, email, dateOfBirth, age, gender, address, emergencyContactName, emergencyContactPhone, joiningDate, profilePhoto, status, trainerId, trainerName, createdAt, updatedAt);
        }
    }
}
