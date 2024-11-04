package com.example.migroscase.contoller;

import com.example.migroscase.controller.CourierController;
import com.example.migroscase.dto.Courier;
import com.example.migroscase.service.CourierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourierController.class)
class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierService courierService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        when(courierService.getTotalDistance(anyLong())).thenReturn(10.0);
    }

    @Test
    void logCourierLocation_ShouldReturnSuccessMessage() throws Exception {
        Courier courier = new Courier();
        courier.setCourier(1L);
        courier.setLat(40.0);
        courier.setLng(30.0);
        mockMvc.perform(post("/couriers/log-location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courier)))
                .andExpect(status().isOk())
                .andExpect(content().string("Courier location logged."));
    }

    @Test
    void getTotalDistance_ShouldReturnTotalDistance() throws Exception {
        mockMvc.perform(get("/couriers/1/total-distance"))
                .andExpect(status().isOk())
                .andExpect(content().string("10.0"));
    }
}