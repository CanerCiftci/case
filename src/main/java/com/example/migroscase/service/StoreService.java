package com.example.migroscase.service;

import com.example.migroscase.dto.Store;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreService {
    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);
    private List<Store> stores = new ArrayList<>();

    @EventListener(ApplicationReadyEvent.class)
    public void loadStores() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("stores.json")) {
            stores = objectMapper.readValue(inputStream, new TypeReference<List<Store>>() {});
            validateStores(stores);
        } catch (IOException e) {
            logger.error("JSON dosyası yüklenirken bir hata oluştu: {}", e.getMessage(), e);
        }
    }

    private void validateStores(List<Store> stores) {
        List<Store> invalidStoreList = stores.stream().filter(store -> store == null || store.getName() == null || store.getName().isEmpty()).toList();

        if (!invalidStoreList.isEmpty()) {
            invalidStoreList.forEach(store -> logger.error("Hata: Mağaza adı null veya boş olamaz. Mağaza: {}", store));
            throw new IllegalArgumentException("Geçersiz mağaza verisi");
        }
    }

    public List<Store> getStores() {
        return stores;
    }
}
