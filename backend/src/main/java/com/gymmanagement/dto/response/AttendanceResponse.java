package com.gymmanagement.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AttendanceResponse {
    private Long id;
    private Long memberDbId;
    private String memberId;
    private String memberName;
    private String memberPhone;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private LocalDate date;
    private String status;
    private LocalDateTime createdAt;

    public AttendanceResponse() {}

    public AttendanceResponse(Long id, Long memberDbId, String memberId, String memberName, String memberPhone, LocalDateTime checkInTime, LocalDateTime checkOutTime, LocalDate date, String status, LocalDateTime createdAt) {
        this.id = id;
        this.memberDbId = memberDbId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPhone = memberPhone;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.date = date;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMemberDbId() { return memberDbId; }
    public void setMemberDbId(Long memberDbId) { this.memberDbId = memberDbId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getMemberPhone() { return memberPhone; }
    public void setMemberPhone(String memberPhone) { this.memberPhone = memberPhone; }

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
        private Long memberDbId;
        private String memberId;
        private String memberName;
        private String memberPhone;
        private LocalDateTime checkInTime;
        private LocalDateTime checkOutTime;
        private LocalDate date;
        private String status;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder memberDbId(Long memberDbId) { this.memberDbId = memberDbId; return this; }
        public Builder memberId(String memberId) { this.memberId = memberId; return this; }
        public Builder memberName(String memberName) { this.memberName = memberName; return this; }
        public Builder memberPhone(String memberPhone) { this.memberPhone = memberPhone; return this; }
        public Builder checkInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; return this; }
        public Builder checkOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; return this; }
        public Builder date(LocalDate date) { this.date = date; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AttendanceResponse build() {
            return new AttendanceResponse(id, memberDbId, memberId, memberName, memberPhone, checkInTime, checkOutTime, date, status, createdAt);
        }
    }
}
