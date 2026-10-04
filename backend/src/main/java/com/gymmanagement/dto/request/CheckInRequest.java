package com.gymmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CheckInRequest {
    @NotBlank(message = "Member ID is required")
    private String memberId;

    public CheckInRequest() {}

    public CheckInRequest(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
}
