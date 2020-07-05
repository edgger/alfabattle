package com.github.edgger.alfabattle.task5.service;

import com.github.edgger.alfabattle.task5.dto.PromoMatrix;
import org.springframework.stereotype.Service;

@Service
public class PromoService {

    private PromoMatrix promoMatrix = new PromoMatrix();

    public void savePromoMatrix(PromoMatrix promoMatrix) {
        this.promoMatrix = promoMatrix;
    }

    public PromoMatrix getPromoMatrix() {
        return promoMatrix;
    }
}
