package com.github.edgger.alfabattle.task4.controller;

import com.github.edgger.alfabattle.task4.dto.StatusDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/admin/health")
    public ResponseEntity<StatusDto> health() {
        return ResponseEntity.ok(new StatusDto("UP"));
    }

}
