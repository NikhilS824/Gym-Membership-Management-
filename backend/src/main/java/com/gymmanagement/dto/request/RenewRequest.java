package com.gymmanagement.dto.request;

import com.gymmanagement.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class RenewRequest {
    @NotNull(message = "Plan ID is required")
    private Long planId;

    private BigDecimal discount;
    private PaymentMethod paymentMethod;
    private String transactionReference;
    private String notes;

    public RenewRequest() {}

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
