package com.github.edgger.alfabattle.task3.controller;

import com.github.edgger.alfabattle.task3.dto.BranchDto;
import com.github.edgger.alfabattle.task3.dto.BranchDtoWithPredicting;
import com.github.edgger.alfabattle.task3.dto.ErrorResponse;
import com.github.edgger.alfabattle.task3.exception.BranchNotFoundException;
import com.github.edgger.alfabattle.task3.service.BranchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/")
public class BranchController {

    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping("/branches/{id}")
    public ResponseEntity<BranchDto> getBranch(@PathVariable Long id) {
        log.debug("get branch {}", id);
        BranchDto branchDto = branchService.getBranchById(id);
        return ResponseEntity.ok(branchDto);
    }

    @GetMapping("/branches")
    public ResponseEntity<BranchDto> getBranch(@RequestParam("lat") Double lat,
                                               @RequestParam("lon") Double lon) {
        log.debug("get nearest branch lan:{} lon:{}", lat, lon);
        BranchDto branchById = branchService.getNearestBranch(lat, lon);
        return ResponseEntity.ok(branchById);
    }

    @GetMapping("branches/{id}/predict")
    public ResponseEntity<BranchDtoWithPredicting> getBranchWithPredicting(@PathVariable Long id,
                                                                           @RequestParam("dayOfWeek") Integer dayOfWeek,
                                                                           @RequestParam("hourOfDay") Integer hourOfDay) {
        log.debug("get branch {} with predicting {} {}", id, dayOfWeek, hourOfDay);
        BranchDtoWithPredicting branchWithPredicting = branchService.getBranchWithPredicting(id, dayOfWeek, hourOfDay);
        return ResponseEntity.ok(branchWithPredicting);
    }

    @ExceptionHandler({BranchNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleBranchNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("branch not found"));
    }

}
