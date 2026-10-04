package com.gymmanagement.dto.response;

import com.gymmanagement.enums.MembershipStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MembershipResponse {
    private Long id;
    private Long memberDbId;
    private String memberId;
    private String memberName;
    private Long planId;
    private String planName;
    private LocalDate startDate;
    private LocalDate endDate;
    private MembershipStatus status;
    private BigDecimal amount;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private long daysRemaining;
    private boolean isExpired;
    private boolean isExpiringSoon;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MembershipResponse() {}

    public MembershipResponse(Long id, Long memberDbId, String memberId, String memberName, Long planId, String planName, LocalDate startDate, LocalDate endDate, MembershipStatus status, BigDecimal amount, BigDecimal discount, BigDecimal finalAmount, long daysRemaining, boolean isExpired, boolean isExpiringSoon, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.memberDbId = memberDbId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.planId = planId;
        this.planName = planName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.amount = amount;
        this.discount = discount;
        this.finalAmount = finalAmount;
        this.daysRemaining = daysRemaining;
        this.isExpired = isExpired;
        this.isExpiringSoon = isExpiringSoon;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMemberDbId() { return memberDbId; }
    public void setMemberDbId(Long memberDbId) { this.memberDbId = memberDbId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public MembershipStatus getStatus() { return status; }
    public void setStatus(MembershipStatus status) { this.status = status; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }

    public long getDaysRemaining() { return daysRemaining; }
    public void setDaysRemaining(long daysRemaining) { this.daysRemaining = daysRemaining; }

    public boolean isExpired() { return isExpired; }
    public void setExpired(boolean expired) { isExpired = expired; }

    public boolean isExpiringSoon() { return isExpiringSoon; }
    public void setExpiringSoon(boolean expiringSoon) { isExpiringSoon = expiringSoon; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long memberDbId;
        private String memberId;
        private String memberName;
        private Long planId;
        private String planName;
        private LocalDate startDate;
        private LocalDate endDate;
        private MembershipStatus status;
        private BigDecimal amount;
        private BigDecimal discount;
        private BigDecimal finalAmount;
        private long daysRemaining;
        private boolean isExpired;
        private boolean isExpiringSoon;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder memberDbId(Long memberDbId) { this.memberDbId = memberDbId; return this; }
        public Builder memberId(String memberId) { this.memberId = memberId; return this; }
        public Builder memberName(String memberName) { this.memberName = memberName; return this; }
        public Builder planId(Long planId) { this.planId = planId; return this; }
        public Builder planName(String planName) { this.planName = planName; return this; }
        public Builder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public Builder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public Builder status(MembershipStatus status) { this.status = status; return this; }
        public Builder amount(BigDecimal amount) { this.amount = amount; return this; }
        public Builder discount(BigDecimal discount) { this.discount = discount; return this; }
        public Builder finalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; return this; }
        public Builder daysRemaining(long daysRemaining) { this.daysRemaining = daysRemaining; return this; }
        public Builder isExpired(boolean isExpired) { this.isExpired = isExpired; return this; }
        public Builder isExpiringSoon(boolean isExpiringSoon) { this.isExpiringSoon = isExpiringSoon; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public MembershipResponse build() {
            return new MembershipResponse(id, memberDbId, memberId, memberName, planId, planName, startDate, endDate, status, amount, discount, finalAmount, daysRemaining, isExpired, isExpiringSoon, createdAt, updatedAt);
        }
    }
}
