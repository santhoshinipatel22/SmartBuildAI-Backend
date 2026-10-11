package com.smartbuild.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "consumptions")
public class Consumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Material reference is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "material_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Material material;

    @NotNull(message = "Consumption quantity is required")
    @DecimalMin(value = "0.01", message = "Consumption quantity must be greater than zero")
    @Column(nullable = false)
    private Double quantity;

    @Column(name = "consumption_date", nullable = false)
    private LocalDateTime consumptionDate;

    public Consumption() {
    }

    public Consumption(Material material, Double quantity) {
        this.material = material;
        this.quantity = quantity;
        this.consumptionDate = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.consumptionDate == null) {
            this.consumptionDate = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getConsumptionDate() {
        return consumptionDate;
    }

    public void setConsumptionDate(LocalDateTime consumptionDate) {
        this.consumptionDate = consumptionDate;
    }
}
