package com.example.migroscase.controller;

import com.example.migroscase.dto.Courier;
import com.example.migroscase.service.CourierService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/couriers")
public class CourierController {
    private final CourierService courierService;

    @PostMapping("/log-location")
    public String logCourierLocation(@RequestBody Courier courier) {
        courierService.logCourierLocation(courier);
        return "Courier location logged.";
    }

    @GetMapping("/{courierId}/total-distance")
    public double getTotalDistance(@PathVariable Long courierId) {
        return courierService.getTotalDistance(courierId);
    }
}