package com.github.edgger.alfabattle.task2.controller;

import com.github.edgger.alfabattle.task2.exceptions.UserNotFoundException;
import com.github.edgger.alfabattle.task2.model.StatusResponse;
import com.github.edgger.alfabattle.task2.model.UserPaymentAnalytic;
import com.github.edgger.alfabattle.task2.model.UserPaymentStats;
import com.github.edgger.alfabattle.task2.model.UserTemplate;
import com.github.edgger.alfabattle.task2.service.AnalyticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
public class AnalyticController {

    private final AnalyticService analyticService;

    @Autowired
    public AnalyticController(AnalyticService analyticService) {
        this.analyticService = analyticService;
    }

    @GetMapping("/admin/health")
    public ResponseEntity<StatusResponse> health() {
        return ResponseEntity.ok(new StatusResponse("UP"));
    }

    @GetMapping("/analytic")
    public ResponseEntity<Collection<UserPaymentAnalytic>> getAnalytic() throws Exception {
        Collection<UserPaymentAnalytic> result = analyticService.getAnalytic();
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/analytic/{userId}")
    public ResponseEntity<UserPaymentAnalytic> getAnalyticUser(@PathVariable String userId) throws Exception {
        UserPaymentAnalytic result = analyticService.getAnalyticByUserId(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/analytic/{userId}/stats")
    public ResponseEntity<UserPaymentStats> getAnalyticUserStats(@PathVariable String userId) throws Exception {
        UserPaymentStats result = analyticService.getStatsByUserId(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/analytic/{userId}/templates")
    public ResponseEntity<Collection<UserTemplate>> getAnalyticUserTemplates(@PathVariable String userId) throws Exception {
        Collection<UserTemplate> result = analyticService.getTemplatesByUserId(userId);
        return ResponseEntity.ok(result) ;
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<StatusResponse> handleUserNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new StatusResponse("User not found"));
    }

    @PostMapping("/analytic")
    public void addAnalytic() throws Exception {
        analyticService.addAnalytic();
    }
}
