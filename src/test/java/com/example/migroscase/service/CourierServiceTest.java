package com.example.migroscase.service;

import com.example.migroscase.dto.Courier;
import com.example.migroscase.dto.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourierServiceTest {
    private CourierService courierService;
    private StoreService storeService;

    @BeforeEach
    void setUp() {
        storeService = mock(StoreService.class);
        courierService = new CourierService(storeService);
        List<Store> mockStores = new ArrayList<>();
        Store store1 = new Store();
        store1.setName("Atasehir MMM Migros");
        store1.setLat(40.9923307);
        store1.setLng(29.1244229);
        mockStores.add(store1);
        Store store2 = new Store();
        store2.setName("Novada MMM Migros");
        store2.setLat(40.986106);
        store2.setLng(29.1161293);
        mockStores.add(store2);
        when(storeService.getStores()).thenReturn(mockStores);
    }

    @Test
    void testLogCourierLocationValid() {
        Courier courier = new Courier();
        courier.setCourier(1L);
        courier.setLat(40.9924);
        courier.setLng(29.1245);
        courierService.logCourierLocation(courier);
        assertEquals(0.0, courierService.getTotalDistance(1L));
    }

    @Test
    void testLogCourierLocationInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {courierService.logCourierLocation(null);
            }, "Kurye verisi null olamaz.");

        Courier invalidCourier = new Courier();
        invalidCourier.setCourier(null); // Kurye ID'si null
        assertThrows(IllegalArgumentException.class, () -> {courierService.logCourierLocation(invalidCourier);
        }, "Geçersiz kurye verisi.");
    }

    @Test
    void testGetTotalDistance() {
        Courier courier = new Courier();
        courier.setCourier(1L);
        courier.setLat(40.9924);
        courier.setLng(29.1245);
        courierService.logCourierLocation(courier);
        Courier courier2 = new Courier();
        courier2.setCourier(1L);
        courier2.setLat(50.9921);
        courier2.setLng(29.1245);
        courierService.logCourierLocation(courier2);
        double totalDistance = courierService.getTotalDistance(1L);
        assertTrue(totalDistance > 0.0);
    }
}
