package com.github.edgger.alfabattle.task1.controller;

import com.github.edgger.alfabattle.task1.model.AtmResponse;
import com.github.edgger.alfabattle.task1.service.AtmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AtmController {

    private final AtmService service;

    @Autowired
    public AtmController(AtmService service) {
        this.service = service;
    }

    @GetMapping(value = "/atms/{deviceId}")
    public ResponseEntity<AtmResponse> get(@PathVariable("deviceId") int deviceId) {
        AtmResponse atmResponse = service.get(deviceId);
        return ResponseEntity.ok(atmResponse);
    }

    @GetMapping(value = "/atms/nearest")
    public ResponseEntity<AtmResponse> getNearest(@RequestParam("latitude") Double latitude,
                                                  @RequestParam("longitude") Double longitude,
                                                  @RequestParam(value = "payments",defaultValue = "false") boolean payments) {
        AtmResponse atmResponse = service.getNearest(latitude, longitude, payments);
        return ResponseEntity.ok(atmResponse);
    }

    @GetMapping(value = "/atms/nearest-with-alfik")
    public ResponseEntity<List<AtmResponse>> getNearestAlfik(@RequestParam("latitude") Double latitude,
                                                             @RequestParam("longitude") Double longitude,
                                                             @RequestParam("alfik") int alfik) {
        List<AtmResponse> atmResponseList = service.getNearestAlfik(latitude, longitude, alfik);
        return ResponseEntity.ok(atmResponseList);
    }
}
