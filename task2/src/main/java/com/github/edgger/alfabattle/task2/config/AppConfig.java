package com.github.edgger.alfabattle.task2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.listener.LoggingErrorHandler;

@EnableKafka
@Configuration
public class AppConfig {

    @Bean
    public LoggingErrorHandler errorHandler() {
        return new LoggingErrorHandler();
    }
}
