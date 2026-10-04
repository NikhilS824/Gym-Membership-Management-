package com.gymmanagement.dto.response;

import com.gymmanagement.enums.PaymentMethod;
import com.gymmanagement.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {
    private Long id;
    private Long memberDbId;
    private String memberId;
    private String memberName;
    private String memberPhone;
    private String memberEmail;
    private Long membershipId;
    private String planName;
    private BigDecimal amount;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionReference;
    private LocalDateTime paymentDate;
    private String notes;
    private LocalDateTime createdAt;

    public PaymentResponse() {}

    public PaymentResponse(Long id, Long memberDbId, String memberId, String memberName, String memberPhone, String memberEmail, Long membershipId, String planName, BigDecimal amount, BigDecimal discount, BigDecimal finalAmount, PaymentMethod paymentMethod, PaymentStatus paymentStatus, String transactionReference, LocalDateTime paymentDate, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.memberDbId = memberDbId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPhone = memberPhone;
        this.memberEmail = memberEmail;
        this.membershipId = membershipId;
        this.planName = planName;
        this.amount = amount;
        this.discount = discount;
        this.finalAmount = finalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionReference = transactionReference;
        this.paymentDate = paymentDate;
        this.notes = notes;
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

    public String getMemberEmail() { return memberEmail; }
    public void setMemberEmail(String memberEmail) { this.memberEmail = memberEmail; }

    public Long getMembershipId() { return membershipId; }
    public void setMembershipId(Long membershipId) { this.membershipId = membershipId; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long memberDbId;
        private String memberId;
        private String memberName;
        private String memberPhone;
        private String memberEmail;
        private Long membershipId;
        private String planName;
        private BigDecimal amount;
        private BigDecimal discount;
        private BigDecimal finalAmount;
        private PaymentMethod paymentMethod;
        private PaymentStatus paymentStatus;
        private String transactionReference;
        private LocalDateTime paymentDate;
        private String notes;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder memberDbId(Long memberDbId) { this.memberDbId = memberDbId; return this; }
        public Builder memberId(String memberId) { this.memberId = memberId; return this; }
        public Builder memberName(String memberName) { this.memberName = memberName; return this; }
        public Builder memberPhone(String memberPhone) { this.memberPhone = memberPhone; return this; }
        public Builder memberEmail(String memberEmail) { this.memberEmail = memberEmail; return this; }
        public Builder membershipId(Long membershipId) { this.membershipId = membershipId; return this; }
        public Builder planName(String planName) { this.planName = planName; return this; }
        public Builder amount(BigDecimal amount) { this.amount = amount; return this; }
        public Builder discount(BigDecimal discount) { this.discount = discount; return this; }
        public Builder finalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; return this; }
        public Builder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public Builder transactionReference(String transactionReference) { this.transactionReference = transactionReference; return this; }
        public Builder paymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public PaymentResponse build() {
            return new PaymentResponse(id, memberDbId, memberId, memberName, memberPhone, memberEmail, membershipId, planName, amount, discount, finalAmount, paymentMethod, paymentStatus, transactionReference, paymentDate, notes, createdAt);
        }
    }
}
