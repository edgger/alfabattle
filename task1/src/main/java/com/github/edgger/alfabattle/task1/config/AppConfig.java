package com.github.edgger.alfabattle.task1.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;

@Slf4j
@Configuration
public class AppConfig {

    @Value("${client.ssl.key-store:keystore.jks}")
    private String keyStorePath;

    @Value("${client.ssl.key-store-password:pass123}")
    private char[] keyStorePassword;

    @Value("${client.ssl.key-password:pass123}")
    private char[] keyPassword;

    /*@Value("file:keystore.jks")
    private Resource keystoreResource;*/

    @Bean
    public WebSocketStompClient webSocketClient() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        /*List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);*/
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        return stompClient;
    }

    @Bean
    public RestTemplate restTemplate() {
        SSLContext sslContext = getClientSslContext();
        HttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
//                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }

    private SSLContext getClientSslContext() {
        try {
            KeyStore keyStore = loadKeyStore(keyStorePath, keyStorePassword);
            return new SSLContextBuilder()
                    .loadKeyMaterial(keyStore, keyPassword)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyStore loadKeyStore(String keystorePath, char[] keystorePassword) throws Exception {
        log.info("Current workdir: {}", System.getProperty("user.dir"));
//        File keystoreFile = ResourceUtils.getFile("file:keystore.jks");
        Resource resource = new FileSystemResource(keystorePath);
        if (resource.exists()) {
            log.info("Loading KeyStore from FileSystemResource: {}", resource.getFile().getAbsolutePath());
        } else {
            resource = new ClassPathResource("keystore.jks");
            log.info("Loading default KeyStore from ClassPathResource: {}", resource.getFilename());
        }

        try (InputStream inputStream = resource.getInputStream()) {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(inputStream, keystorePassword);
            return keystore;
        }
    }
}
