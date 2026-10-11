package com.smartbuild.service;

import com.smartbuild.dto.ConsumptionRequest;
import com.smartbuild.entity.Consumption;
import com.smartbuild.entity.Material;
import com.smartbuild.exception.InvalidOperationException;
import com.smartbuild.exception.ResourceNotFoundException;
import com.smartbuild.repository.ConsumptionRepository;
import com.smartbuild.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ConsumptionService {

    private final ConsumptionRepository consumptionRepository;
    private final MaterialRepository materialRepository;

    public ConsumptionService(ConsumptionRepository consumptionRepository, MaterialRepository materialRepository) {
        this.consumptionRepository = consumptionRepository;
        this.materialRepository = materialRepository;
    }

    public Consumption recordConsumption(ConsumptionRequest request) {
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new InvalidOperationException("Consumption quantity must be strictly greater than zero.");
        }

        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with ID: " + request.getMaterialId()));

        double availableStock = material.getCurrentStock() != null ? material.getCurrentStock() : 0.0;
        double requestedQuantity = request.getQuantity();

        if (requestedQuantity > availableStock) {
            throw new InvalidOperationException(String.format(
                    "Insufficient stock for '%s'. Available: %s %s, requested: %s %s.",
                    material.getName(),
                    availableStock,
                    material.getUnit(),
                    requestedQuantity,
                    material.getUnit()
            ));
        }

        double updatedStock = availableStock - requestedQuantity;
        if (updatedStock < 0) {
            throw new InvalidOperationException("Operation would result in negative stock. Transaction aborted.");
        }

        material.setCurrentStock(updatedStock);
        materialRepository.save(material);

        Consumption consumption = new Consumption();
        consumption.setMaterial(material);
        consumption.setQuantity(requestedQuantity);
        consumption.setConsumptionDate(LocalDateTime.now());

        return consumptionRepository.save(consumption);
    }

    @Transactional(readOnly = true)
    public List<Consumption> getAllConsumptions() {
        return consumptionRepository.findAllByOrderByConsumptionDateDesc();
    }
}
