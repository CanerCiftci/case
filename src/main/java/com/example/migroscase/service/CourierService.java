package com.example.migroscase.service;

import com.example.migroscase.constants.Constants;
import com.example.migroscase.dto.Courier;
import com.example.migroscase.dto.Store;
import com.example.migroscase.util.CalculationUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CourierService {
    private static final Logger logger = LoggerFactory.getLogger(CourierService.class);

    private final Map<Long, Courier> lastCourierLocations = new HashMap <>();
    private final Map<Long, Double> courierDistances = new HashMap <>();
    private final Map<Long, String> lastStoreEntries = new HashMap <>();
    private final Map<Long, Long> lastEntryTimes = new HashMap<>();

    private final StoreService storeService;

    public void logCourierLocation(Courier courier) {
        if (courier == null || courier.getCourier() == null) {
            throw new IllegalArgumentException("Geçersiz kurye verisi");
        }
        updateCourierLocationAndDistance(courier);
        checkForStoreEntry(courier);
    }

    private void updateCourierLocationAndDistance(Courier courier) {
        if (lastCourierLocations.containsKey(courier.getCourier())) {
            updateCourierDistance(courier);
        }
        lastCourierLocations.put(courier.getCourier(), courier);
    }

    @Async
    protected void updateCourierDistance(Courier courier) {
        Courier lastLocation = lastCourierLocations.get(courier.getCourier());
        if (lastLocation != null) {
            double distance = CalculationUtil.calculateDistanceByHaversine(
                    lastLocation.getLat(), lastLocation.getLng(),
                    courier.getLat(), courier.getLng()
            );
            courierDistances.put(courier.getCourier(),
                    courierDistances.getOrDefault(courier.getCourier(), 0.0) + distance);
        }
    }

    @Async
    protected void checkForStoreEntry(Courier courier) {
        List<Store> stores = storeService.getStores();
        for (Store store : stores) {
            double distanceToStore = CalculationUtil.calculateDistanceByHaversine(
                    courier.getLat(), courier.getLng(), store.getLat(), store.getLng()
            );
            if (distanceToStore <= Constants.STORE_ENTRY_DISTANCE_THRESHOLD &&
                    !hasRecentEntry(courier.getCourier(), store.getName())) {
                logger.info("Kurye ID: {} {} mağazasına giriş yaptı.", courier.getCourier(), store.getName());
                updateCourierStoreEntry(courier.getCourier(), store.getName());
                break;
            }
        }
    }

    private boolean hasRecentEntry(Long courierId, String storeName) {
        return lastStoreEntries.containsKey(courierId) &&
                lastStoreEntries.get(courierId).equals(storeName) &&
                (getCurrentTime() - lastEntryTimes.getOrDefault(courierId, 0L) < Constants.STORE_ENTRY_TIME_THRESHOLD);
    }

    @Async
    protected void updateCourierStoreEntry(Long courierId, String storeName) {
        lastEntryTimes.put(courierId, getCurrentTime());
        lastStoreEntries.put(courierId, storeName);
    }

    private static long getCurrentTime() {
        return System.currentTimeMillis() / Constants.TIME_CONVERSION_FACTOR;
    }

    public double getTotalDistance(Long courierId) {
        return courierDistances.getOrDefault(courierId, 0.0);
    }
}
