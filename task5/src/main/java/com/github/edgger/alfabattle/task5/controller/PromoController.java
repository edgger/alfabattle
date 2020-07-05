package com.github.edgger.alfabattle.task5.controller;

import com.github.edgger.alfabattle.task5.dto.PromoMatrix;
import com.github.edgger.alfabattle.task5.service.PromoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromoController {

    private final PromoService promoService;

    public PromoController(PromoService promoService) {
        this.promoService = promoService;
    }

    @PostMapping("/promo")
    public void savePromoMatrix(@RequestBody PromoMatrix promoMatrix) {
        promoService.savePromoMatrix(promoMatrix);
    }
}
