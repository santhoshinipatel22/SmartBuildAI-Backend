package com.smartbuild.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MaterialRequest {

    @NotBlank(message = "Material name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Unit is required")
    private String unit;

    @NotNull(message = "Current stock is required")
    @DecimalMin(value = "0.0", message = "Current stock cannot be negative")
    private Double currentStock;

    @NotNull(message = "Reorder level is required")
    @DecimalMin(value = "0.0", message = "Reorder level cannot be negative")
    private Double reorderLevel;

    public MaterialRequest() {
    }

    public MaterialRequest(String name, String category, String unit, Double currentStock, Double reorderLevel) {
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.currentStock = currentStock;
        this.reorderLevel = reorderLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Double currentStock) {
        this.currentStock = currentStock;
    }

    public Double getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Double reorderLevel) {
        this.reorderLevel = reorderLevel;
    }
}
