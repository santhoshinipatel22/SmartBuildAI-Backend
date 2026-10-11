package com.smartbuild.controller;

import com.smartbuild.dto.MaterialRequest;
import com.smartbuild.entity.Material;
import com.smartbuild.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category) {
        List<Material> materials = materialService.getAllMaterials(search, category);
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Material>> getLowStockMaterials() {
        List<Material> lowStockList = materialService.getLowStockMaterials();
        return ResponseEntity.ok(lowStockList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long id) {
        Material material = materialService.getMaterialById(id);
        return ResponseEntity.ok(material);
    }

    @PostMapping
    public ResponseEntity<Material> createMaterial(@Valid @RequestBody MaterialRequest request) {
        Material created = materialService.createMaterial(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(
            @PathVariable Long id,
            @Valid @RequestBody MaterialRequest request) {
        Material updated = materialService.updateMaterial(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.ok().build();
    }
}
