package com.smartbuild.service;

import com.smartbuild.dto.ConsumptionRequest;
import com.smartbuild.entity.Consumption;
import com.smartbuild.entity.Material;
import com.smartbuild.exception.InvalidOperationException;
import com.smartbuild.repository.ConsumptionRepository;
import com.smartbuild.repository.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsumptionServiceTest {

    @Mock
    private ConsumptionRepository consumptionRepository;

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private ConsumptionService consumptionService;

    private Material sampleMaterial;

    @BeforeEach
    void setUp() {
        sampleMaterial = new Material("Cement", "Binding Material", "Bags", 100.0, 20.0);
        sampleMaterial.setId(1L);
    }

    @Test
    void testRecordConsumptionSuccess() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(sampleMaterial));
        when(materialRepository.save(any(Material.class))).thenReturn(sampleMaterial);
        when(consumptionRepository.save(any(Consumption.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ConsumptionRequest request = new ConsumptionRequest(1L, 85.0);
        Consumption result = consumptionService.recordConsumption(request);

        assertNotNull(result);
        assertEquals(15.0, sampleMaterial.getCurrentStock(), 0.001);
        assertEquals(85.0, result.getQuantity(), 0.001);
        verify(materialRepository, times(1)).save(sampleMaterial);
        verify(consumptionRepository, times(1)).save(any(Consumption.class));
    }

    @Test
    void testRecordConsumptionInsufficientStock() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(sampleMaterial));

        ConsumptionRequest request = new ConsumptionRequest(1L, 150.0);

        InvalidOperationException ex = assertThrows(InvalidOperationException.class, () -> {
            consumptionService.recordConsumption(request);
        });

        assertTrue(ex.getMessage().contains("Insufficient stock"));
        assertEquals(100.0, sampleMaterial.getCurrentStock(), 0.001);
        verify(materialRepository, never()).save(any());
        verify(consumptionRepository, never()).save(any());
    }

    @Test
    void testRecordConsumptionZeroOrNegativeQuantity() {
        ConsumptionRequest request = new ConsumptionRequest(1L, -10.0);

        assertThrows(InvalidOperationException.class, () -> {
            consumptionService.recordConsumption(request);
        });
    }
}
