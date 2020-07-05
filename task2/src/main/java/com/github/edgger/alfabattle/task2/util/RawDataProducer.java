package com.github.edgger.alfabattle.task2.util;

import com.github.edgger.alfabattle.task2.model.RawData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RawDataProducer {

    @Value("${kafka.listener.topic}")
    private String topic;

    private final KafkaTemplate<String, RawData> kafkaTemplate;

    @Autowired
    public RawDataProducer(KafkaTemplate<String, RawData> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produce() {
        kafkaTemplate.send(topic, "123", new RawData());
    }
}
