package com.smartbuild.service;

import com.smartbuild.dto.DashboardSummaryDto;
import com.smartbuild.entity.Material;
import com.smartbuild.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final MaterialRepository materialRepository;

    public DashboardService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public DashboardSummaryDto getDashboardSummary() {
        List<Material> allMaterials = materialRepository.findAllByOrderByCreatedAtDesc();

        long totalMaterials = allMaterials.size();

        double totalStock = 0.0;
        long lowStockCount = 0;
        long outOfStockCount = 0;
        Map<String, Double> stockByUnit = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        List<Material> lowStockAlerts = new ArrayList<>();

        for (Material m : allMaterials) {
            double stock = m.getCurrentStock() != null ? m.getCurrentStock() : 0.0;
            double reorder = m.getReorderLevel() != null ? m.getReorderLevel() : 0.0;

            totalStock += stock;

            String unit = m.getUnit() != null ? m.getUnit().trim() : "Units";
            stockByUnit.put(unit, stockByUnit.getOrDefault(unit, 0.0) + stock);

            if (stock <= 0) {
                outOfStockCount++;
                lowStockAlerts.add(m);
            } else if (stock <= reorder) {
                lowStockCount++;
                lowStockAlerts.add(m);
            }
        }

        // Sort alerts: Out of stock first, then lowest stock relative to reorder
        lowStockAlerts.sort(Comparator.comparingDouble(m -> (m.getCurrentStock() != null ? m.getCurrentStock() : 0.0)));

        List<Material> recentlyAdded = allMaterials.stream()
                .limit(5)
                .collect(Collectors.toList());

        return new DashboardSummaryDto(
                totalMaterials,
                totalStock,
                lowStockCount,
                outOfStockCount,
                stockByUnit,
                recentlyAdded,
                lowStockAlerts
        );
    }
}
