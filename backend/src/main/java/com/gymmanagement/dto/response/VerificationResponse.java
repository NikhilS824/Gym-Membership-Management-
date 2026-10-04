package com.gymmanagement.dto.response;

import java.time.LocalDate;

public class VerificationResponse {
    private String memberId;
    private String memberName;
    private String phone;
    private String email;
    private String profilePhoto;
    private String planName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String verificationStatus;
    private long daysRemaining;
    private String message;
    private boolean allowAccess;

    public VerificationResponse() {}

    public VerificationResponse(String memberId, String memberName, String phone, String email, String profilePhoto, String planName, LocalDate startDate, LocalDate endDate, String verificationStatus, long daysRemaining, String message, boolean allowAccess) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.phone = phone;
        this.email = email;
        this.profilePhoto = profilePhoto;
        this.planName = planName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.verificationStatus = verificationStatus;
        this.daysRemaining = daysRemaining;
        this.message = message;
        this.allowAccess = allowAccess;
    }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfilePhoto() { return profilePhoto; }
    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

    public long getDaysRemaining() { return daysRemaining; }
    public void setDaysRemaining(long daysRemaining) { this.daysRemaining = daysRemaining; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isAllowAccess() { return allowAccess; }
    public void setAllowAccess(boolean allowAccess) { this.allowAccess = allowAccess; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String memberId;
        private String memberName;
        private String phone;
        private String email;
        private String profilePhoto;
        private String planName;
        private LocalDate startDate;
        private LocalDate endDate;
        private String verificationStatus;
        private long daysRemaining;
        private String message;
        private boolean allowAccess;

        public Builder memberId(String memberId) { this.memberId = memberId; return this; }
        public Builder memberName(String memberName) { this.memberName = memberName; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder profilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; return this; }
        public Builder planName(String planName) { this.planName = planName; return this; }
        public Builder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public Builder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public Builder verificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; return this; }
        public Builder daysRemaining(long daysRemaining) { this.daysRemaining = daysRemaining; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder allowAccess(boolean allowAccess) { this.allowAccess = allowAccess; return this; }

        public VerificationResponse build() {
            return new VerificationResponse(memberId, memberName, phone, email, profilePhoto, planName, startDate, endDate, verificationStatus, daysRemaining, message, allowAccess);
        }
    }
}
