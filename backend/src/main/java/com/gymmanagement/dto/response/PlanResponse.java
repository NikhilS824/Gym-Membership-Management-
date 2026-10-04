package com.gymmanagement.dto.response;

import com.gymmanagement.enums.DurationUnit;
import com.gymmanagement.enums.PlanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PlanResponse {
    private Long id;
    private String name;
    private Integer durationValue;
    private DurationUnit durationUnit;
    private BigDecimal price;
    private String description;
    private String benefits;
    private PlanStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PlanResponse() {}

    public PlanResponse(Long id, String name, Integer durationValue, DurationUnit durationUnit, BigDecimal price, String description, String benefits, PlanStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.durationValue = durationValue;
        this.durationUnit = durationUnit;
        this.price = price;
        this.description = description;
        this.benefits = benefits;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getDurationValue() { return durationValue; }
    public void setDurationValue(Integer durationValue) { this.durationValue = durationValue; }

    public DurationUnit getDurationUnit() { return durationUnit; }
    public void setDurationUnit(DurationUnit durationUnit) { this.durationUnit = durationUnit; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }

    public PlanStatus getStatus() { return status; }
    public void setStatus(PlanStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private Integer durationValue;
        private DurationUnit durationUnit;
        private BigDecimal price;
        private String description;
        private String benefits;
        private PlanStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder durationValue(Integer durationValue) { this.durationValue = durationValue; return this; }
        public Builder durationUnit(DurationUnit durationUnit) { this.durationUnit = durationUnit; return this; }
        public Builder price(BigDecimal price) { this.price = price; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder benefits(String benefits) { this.benefits = benefits; return this; }
        public Builder status(PlanStatus status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public PlanResponse build() {
            return new PlanResponse(id, name, durationValue, durationUnit, price, description, benefits, status, createdAt, updatedAt);
        }
    }
}
