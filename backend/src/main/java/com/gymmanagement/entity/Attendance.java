package com.gymmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Member member;

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 20)
    private String status = "PRESENT";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Attendance() {}

    public Attendance(Long id, Member member, LocalDateTime checkInTime, LocalDateTime checkOutTime, LocalDate date, String status, LocalDateTime createdAt) {
        this.id = id;
        this.member = member;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.date = date;
        this.status = status != null ? status : "PRESENT";
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.checkInTime == null) {
            this.checkInTime = LocalDateTime.now();
        }
        if (this.date == null) {
            this.date = this.checkInTime.toLocalDate();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Member member;
        private LocalDateTime checkInTime;
        private LocalDateTime checkOutTime;
        private LocalDate date;
        private String status = "PRESENT";
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder member(Member member) { this.member = member; return this; }
        public Builder checkInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; return this; }
        public Builder checkOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; return this; }
        public Builder date(LocalDate date) { this.date = date; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Attendance build() {
            return new Attendance(id, member, checkInTime, checkOutTime, date, status, createdAt);
        }
    }
}
