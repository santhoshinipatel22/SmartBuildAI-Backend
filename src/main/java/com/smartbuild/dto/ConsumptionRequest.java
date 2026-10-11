package com.smartbuild.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class ConsumptionRequest {

    @NotNull(message = "Material ID is required")
    private Long materialId;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.01", message = "Consumption quantity must be greater than zero")
    private Double quantity;

    public ConsumptionRequest() {
    }

    public ConsumptionRequest(Long materialId, Double quantity) {
        this.materialId = materialId;
        this.quantity = quantity;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
