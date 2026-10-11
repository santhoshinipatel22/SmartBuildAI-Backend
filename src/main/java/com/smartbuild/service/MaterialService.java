package com.smartbuild.service;

import com.smartbuild.dto.MaterialRequest;
import com.smartbuild.entity.Material;
import com.smartbuild.exception.ResourceNotFoundException;
import com.smartbuild.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Transactional(readOnly = true)
    public List<Material> getAllMaterials(String search, String category) {
        List<Material> list = materialRepository.findAllByOrderByCreatedAtDesc();

        if (search != null && !search.trim().isEmpty()) {
            String query = search.trim().toLowerCase();
            list = list.stream()
                    .filter(m -> m.getName().toLowerCase().contains(query))
                    .collect(Collectors.toList());
        }

        if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("ALL")) {
            String cat = category.trim().toLowerCase();
            list = list.stream()
                    .filter(m -> m.getCategory().toLowerCase().equals(cat))
                    .collect(Collectors.toList());
        }

        return list;
    }

    @Transactional(readOnly = true)
    public Material getMaterialById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with ID: " + id));
    }

    public Material createMaterial(MaterialRequest request) {
        Material material = new Material();
        material.setName(request.getName().trim());
        material.setCategory(request.getCategory().trim());
        material.setUnit(request.getUnit().trim());
        material.setCurrentStock(request.getCurrentStock());
        material.setReorderLevel(request.getReorderLevel());
        material.setCreatedAt(LocalDateTime.now());
        return materialRepository.save(material);
    }

    public Material updateMaterial(Long id, MaterialRequest request) {
        Material existing = getMaterialById(id);
        existing.setName(request.getName().trim());
        existing.setCategory(request.getCategory().trim());
        existing.setUnit(request.getUnit().trim());
        existing.setCurrentStock(request.getCurrentStock());
        existing.setReorderLevel(request.getReorderLevel());
        return materialRepository.save(existing);
    }

    public void deleteMaterial(Long id) {
        Material existing = getMaterialById(id);
        materialRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public List<Material> getLowStockMaterials() {
        return materialRepository.findLowStockMaterials();
    }
}
