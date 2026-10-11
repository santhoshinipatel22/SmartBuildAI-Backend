package com.smartbuild.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "materials")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Material name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String category;

    @NotBlank(message = "Unit is required")
    @Column(nullable = false)
    private String unit;

    @NotNull(message = "Current stock is required")
    @DecimalMin(value = "0.0", message = "Current stock cannot be negative")
    @Column(name = "current_stock", nullable = false)
    private Double currentStock;

    @NotNull(message = "Reorder level is required")
    @DecimalMin(value = "0.0", message = "Reorder level cannot be negative")
    @Column(name = "reorder_level", nullable = false)
    private Double reorderLevel;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Material() {
    }

    public Material(String name, String category, String unit, Double currentStock, Double reorderLevel) {
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.currentStock = currentStock;
        this.reorderLevel = reorderLevel;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.currentStock == null) {
            this.currentStock = 0.0;
        }
        if (this.reorderLevel == null) {
            this.reorderLevel = 0.0;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Transient
    public String getStatus() {
        if (currentStock == null || currentStock <= 0) {
            return "OUT OF STOCK";
        }
        if (currentStock <= reorderLevel) {
            return "LOW STOCK";
        }
        return "IN STOCK";
    }
}
