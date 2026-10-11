package com.smartbuild.dto;

import com.smartbuild.entity.Material;
import java.util.List;
import java.util.Map;

public class DashboardSummaryDto {
    private long totalMaterials;
    private double totalStockQuantity;
    private long lowStockCount;
    private long outOfStockCount;
    private Map<String, Double> stockByUnit;
    private List<Material> recentlyAddedMaterials;
    private List<Material> lowStockAlerts;

    public DashboardSummaryDto() {
    }

    public DashboardSummaryDto(long totalMaterials, double totalStockQuantity, long lowStockCount, 
                               long outOfStockCount, Map<String, Double> stockByUnit, 
                               List<Material> recentlyAddedMaterials, List<Material> lowStockAlerts) {
        this.totalMaterials = totalMaterials;
        this.totalStockQuantity = totalStockQuantity;
        this.lowStockCount = lowStockCount;
        this.outOfStockCount = outOfStockCount;
        this.stockByUnit = stockByUnit;
        this.recentlyAddedMaterials = recentlyAddedMaterials;
        this.lowStockAlerts = lowStockAlerts;
    }

    public long getTotalMaterials() {
        return totalMaterials;
    }

    public void setTotalMaterials(long totalMaterials) {
        this.totalMaterials = totalMaterials;
    }

    public double getTotalStockQuantity() {
        return totalStockQuantity;
    }

    public void setTotalStockQuantity(double totalStockQuantity) {
        this.totalStockQuantity = totalStockQuantity;
    }

    public long getLowStockCount() {
        return lowStockCount;
    }

    public void setLowStockCount(long lowStockCount) {
        this.lowStockCount = lowStockCount;
    }

    public long getOutOfStockCount() {
        return outOfStockCount;
    }

    public void setOutOfStockCount(long outOfStockCount) {
        this.outOfStockCount = outOfStockCount;
    }

    public Map<String, Double> getStockByUnit() {
        return stockByUnit;
    }

    public void setStockByUnit(Map<String, Double> stockByUnit) {
        this.stockByUnit = stockByUnit;
    }

    public List<Material> getRecentlyAddedMaterials() {
        return recentlyAddedMaterials;
    }

    public void setRecentlyAddedMaterials(List<Material> recentlyAddedMaterials) {
        this.recentlyAddedMaterials = recentlyAddedMaterials;
    }

    public List<Material> getLowStockAlerts() {
        return lowStockAlerts;
    }

    public void setLowStockAlerts(List<Material> lowStockAlerts) {
        this.lowStockAlerts = lowStockAlerts;
    }
}
