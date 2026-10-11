package com.smartbuild.repository;

import com.smartbuild.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    List<Material> findAllByOrderByCreatedAtDesc();

    List<Material> findByCategoryIgnoreCase(String category);

    List<Material> findByNameContainingIgnoreCase(String name);

    @Query("SELECT m FROM Material m WHERE m.currentStock <= m.reorderLevel ORDER BY m.currentStock ASC")
    List<Material> findLowStockMaterials();

    @Query("SELECT COUNT(m) FROM Material m WHERE m.currentStock <= 0")
    long countOutOfStockMaterials();

    @Query("SELECT COUNT(m) FROM Material m WHERE m.currentStock > 0 AND m.currentStock <= m.reorderLevel")
    long countLowStockMaterials();
}
