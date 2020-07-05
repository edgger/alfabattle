package com.github.edgger.alfabattle.task5.controller;

import com.github.edgger.alfabattle.task5.dto.FinalPriceReceipt;
import com.github.edgger.alfabattle.task5.dto.ShoppingCart;
import com.github.edgger.alfabattle.task5.service.ReceiptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ReceiptController {

    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/receipt")
    public ResponseEntity<FinalPriceReceipt> getFinalPriceReceipt(@RequestBody ShoppingCart shoppingCart) {
        log.info("getFinalPriceReceipt {}", shoppingCart);
        FinalPriceReceipt result = receiptService.getFinalPriceReceipt(shoppingCart);
        return ResponseEntity.ok(result);
    }
}
