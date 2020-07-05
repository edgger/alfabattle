package com.github.edgger.alfabattle.task1.service;

import com.github.edgger.alfabattle.task1.exceptions.AtmNotFoundException;
import com.github.edgger.alfabattle.task1.model.AlfikResponse;
import com.github.edgger.alfabattle.task1.model.AtmAlfikInfo;
import com.github.edgger.alfabattle.task1.model.AtmResponse;
import com.github.edgger.alfabattle.task1.model.openapi.ATMDetails;
import com.github.edgger.alfabattle.task1.model.openapi.Coordinates;
import com.github.edgger.alfabattle.task1.model.openapi.JSONResponseBankATMDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AtmService {

    private final RestTemplate restTemplate;
    private final AlfikWsClient alfikWsService;

    @Value("${alfa.api.atms.url}")
    private String alfaAtmsApiUrl;

    @Value("${alfa.api.clientId}")
    private String clientId;

    @Autowired
    public AtmService(RestTemplate restTemplate, AlfikWsClient alfikWsService) {
        this.restTemplate = restTemplate;
        this.alfikWsService = alfikWsService;
    }

    //https://api.alfabank.ru/node/237
    public AtmResponse get(int deviceId) {
        JSONResponseBankATMDetails responseBankATMDetails = getATMDetails();

        for (ATMDetails atmDetails : responseBankATMDetails.getData().getAtms()) {
            if (deviceId == atmDetails.getDeviceId()) {
                return mapToAtmResponse(atmDetails);
            }
        }

        throw new AtmNotFoundException();
    }

    public AtmResponse getNearest(Double latitude, Double longitude, boolean payments) {
        JSONResponseBankATMDetails responseBankATMDetails = getATMDetails();

        List<ATMDetails> atmDetailsList = responseBankATMDetails.getData().getAtms();

        return atmDetailsList.stream()
                .filter(atmDetails -> !payments || atmDetails.getServices().getPayments() != null) //todo ???
                .filter(atmDetails -> atmDetails.getCoordinates().getLatitude() != null
                        && atmDetails.getCoordinates().getLongitude() != null)
                .min(Comparator.comparing(atmDetails -> {
                    Coordinates coordinates = atmDetails.getCoordinates();
                    return calculateDistance(latitude, longitude, coordinates.getLatitude(), coordinates.getLongitude());
                }))
                .map(this::mapToAtmResponse)
                .orElseThrow(AtmNotFoundException::new);
    }

    public List<AtmResponse> getNearestAlfik(Double latitude, Double longitude, int alfik) {
        JSONResponseBankATMDetails responseBankATMDetails = getATMDetails();

        List<ATMDetails> atmDetailsList = responseBankATMDetails.getData().getAtms();

        Map<Integer, CompletableFuture<AlfikResponse>> alfikResults = new HashMap<>();
        StompSession stompSession = alfikWsService.getStompSession(alfikResults);

        try {
            AtomicLong sum = new AtomicLong();
            return atmDetailsList.stream()
                    .filter(atmDetails -> atmDetails.getCoordinates().getLatitude() != null
                            && atmDetails.getCoordinates().getLongitude() != null)
                    .sorted(Comparator.comparing(atmDetails -> {
                        Coordinates coordinates = atmDetails.getCoordinates();
                        return calculateDistance(latitude, longitude, coordinates.getLatitude(), coordinates.getLongitude());
                    }))
                    .map(atmDetails -> alfikWsService.getAtmAlfikInfo(stompSession, alfikResults, atmDetails))
                    .filter(atmAlfikInfo -> atmAlfikInfo.getAlfik() > 0)
                    .takeWhile(atmDetails -> sum.getAndAdd(atmDetails.getAlfik()) < alfik)
                    .map(AtmAlfikInfo::getAtmDetails)
                    .map(this::mapToAtmResponse)
                    .collect(Collectors.toList());
        } finally {
            stompSession.disconnect();
        }
    }

    private JSONResponseBankATMDetails getATMDetails() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("x-ibm-client-id", clientId);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<JSONResponseBankATMDetails> responseEntity = restTemplate.exchange(alfaAtmsApiUrl,
                HttpMethod.GET, entity, JSONResponseBankATMDetails.class);

        return responseEntity.getBody();
    }

    private AtmResponse mapToAtmResponse(ATMDetails atmDetails) {
        return AtmResponse.builder()
                .deviceId(atmDetails.getDeviceId())
                .latitude(atmDetails.getCoordinates().getLatitude())
                .longitude(atmDetails.getCoordinates().getLongitude())
                .city(atmDetails.getAddress().getCity())
                .location(atmDetails.getAddress().getLocation())
                .payments(atmDetails.getServices().getPayments() != null) //todo ???
                .build();
    }

    private double calculateDistance(double x1, double y1, double x2, double y2) {
        double ac = Math.abs(y2 - y1);
        double cb = Math.abs(x2 - x1);

        return Math.hypot(ac, cb);
    }

}
