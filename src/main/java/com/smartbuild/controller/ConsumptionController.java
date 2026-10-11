package com.smartbuild.controller;

import com.smartbuild.dto.ConsumptionRequest;
import com.smartbuild.entity.Consumption;
import com.smartbuild.service.ConsumptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumption")
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    public ConsumptionController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    @PostMapping
    public ResponseEntity<Consumption> recordConsumption(@Valid @RequestBody ConsumptionRequest request) {
        Consumption created = consumptionService.recordConsumption(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Consumption>> getAllConsumptions() {
        List<Consumption> consumptions = consumptionService.getAllConsumptions();
        return ResponseEntity.ok(consumptions);
    }
}
