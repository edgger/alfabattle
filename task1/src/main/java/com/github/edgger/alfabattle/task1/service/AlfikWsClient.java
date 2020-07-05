package com.github.edgger.alfabattle.task1.service;

import com.github.edgger.alfabattle.task1.model.AlfikRequest;
import com.github.edgger.alfabattle.task1.model.AlfikResponse;
import com.github.edgger.alfabattle.task1.model.AtmAlfikInfo;
import com.github.edgger.alfabattle.task1.model.openapi.ATMDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AlfikWsClient {

    private final WebSocketStompClient stompClient;

    @Value("${alfa.ws.alfiks.url}")
    private String alfaAlfiksWsUrl;

    @Value("${alfa.ws.alfiks.topic}")
    private String alfaAlfiksWsTopic;

    @Value("${alfa.ws.alfiks.timeout.sec}")
    private Integer alfaAlfiksWsTimeoutSec;

    @Autowired
    public AlfikWsClient(WebSocketStompClient stompClient) {
        this.stompClient = stompClient;
    }

    public AtmAlfikInfo getAtmAlfikInfo(StompSession stompSession,
                                        Map<Integer, CompletableFuture<AlfikResponse>> alfikResults,
                                        ATMDetails atmDetails) {

        log.info("Get info {}", atmDetails.getDeviceId());
        CompletableFuture<AlfikResponse> futureAtmAlfikInfo = new CompletableFuture<>();
        alfikResults.put(atmDetails.getDeviceId(), futureAtmAlfikInfo);

        stompSession.send("/", new AlfikRequest(atmDetails.getDeviceId()));
        try {
            AlfikResponse alfikResponse = futureAtmAlfikInfo.get(alfaAlfiksWsTimeoutSec, TimeUnit.SECONDS);
            return new AtmAlfikInfo(atmDetails, alfikResponse.getAlfik());
        } catch (Exception e) {
            log.warn("AtmAlfikInfo not found {}", atmDetails.getDeviceId());
            return new AtmAlfikInfo(atmDetails, -1L);
        }
    }

    public StompSession getStompSession(StompSessionHandler sessionHandler) {
        try {
            StompSession stompSession = stompClient.connect(alfaAlfiksWsUrl, sessionHandler).get();
            stompSession.subscribe(alfaAlfiksWsTopic, sessionHandler);
            return stompSession;
        } catch (Exception e) {
            log.error("Something went wrong", e);
            throw new RuntimeException(e);
        }
    }

    public StompSession getStompSession(Map<Integer, CompletableFuture<AlfikResponse>> alfikResults) {
        try {
            StompSessionHandler sessionHandler = getStompSessionHandler(alfikResults);
            StompSession stompSession = stompClient.connect(alfaAlfiksWsUrl, sessionHandler).get();
            stompSession.subscribe(alfaAlfiksWsTopic, sessionHandler);
            return stompSession;
        } catch (Exception e) {
            log.error("Something went wrong", e);
            throw new RuntimeException(e);
        }
    }

    public StompSessionHandler getStompSessionHandler(Map<Integer, CompletableFuture<AlfikResponse>> alfikResults) {
        return new StompSessionHandlerAdapter() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return AlfikResponse.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                AlfikResponse alfikResponse = (AlfikResponse) payload;
                if (payload != null) {
                    CompletableFuture<AlfikResponse> futureAtmAlfikInfo = alfikResults.get(alfikResponse.getDeviceId());
                    if (futureAtmAlfikInfo != null) {
                        futureAtmAlfikInfo.complete(alfikResponse);
                    }
                } else {
                    log.info("Empty frame {}", headers);
                }
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                log.error("Something went wrong", exception);
            }
        };
    }
}
