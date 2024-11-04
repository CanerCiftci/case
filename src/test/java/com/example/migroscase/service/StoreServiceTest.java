package com.example.migroscase.service;

import com.example.migroscase.dto.Store;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class StoreServiceTest {

    private StoreService storeService;

    @BeforeEach
    void setUp() {
        storeService = new StoreService();
    }

    @Test
    void testLoadStores_ValidData() throws Exception {
        InputStream validInputStream = getClass().getClassLoader().getResourceAsStream("stores.json");

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readValue(validInputStream, new TypeReference<List<Store>>() {}))
                .thenReturn(createValidStores());

        storeService.loadStores();
        assertFalse(storeService.getStores().isEmpty());
        assertEquals(5, storeService.getStores().size());
    }

    private List<Store> createValidStores() {
        List<Store> stores = new ArrayList<>();
        Store store1 = new Store();
        store1.setName("test1");
        stores.add(store1);

        Store store2 = new Store();
        store2.setName("test2");
        stores.add(store2);
        return stores;
    }

    private List<Store> createInvalidStores() {
        List<Store> stores = new ArrayList<>();
        Store store1 = new Store();
        store1.setName(null);

        stores.add(store1);
        return stores;
    }
}
