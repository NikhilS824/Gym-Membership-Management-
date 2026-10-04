package com.gymmanagement.dto.request;

import com.gymmanagement.enums.DurationUnit;
import com.gymmanagement.enums.PlanStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PlanRequest {
    @NotBlank(message = "Plan name is required")
    private String name;

    @NotNull(message = "Duration value is required")
    @Min(value = 1, message = "Duration must be greater than 0")
    private Integer durationValue;

    @NotNull(message = "Duration unit is required")
    private DurationUnit durationUnit;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    private BigDecimal price;

    private String description;
    private String benefits;
    private PlanStatus status;

    public PlanRequest() {}

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
}
