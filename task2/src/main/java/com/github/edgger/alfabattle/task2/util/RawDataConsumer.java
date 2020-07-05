package com.github.edgger.alfabattle.task2.util;

import com.github.edgger.alfabattle.task2.model.RawData;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//https://asbnotebook.com/2019/10/28/apache-kafka-producer-consumer-example-with-spring-boot/
//https://medium.com/nycdev/java-use-spring-boot-to-consume-and-produce-data-streams-with-apache-kafka-24f4500125f7
//https://docs.spring.io/spring-kafka/docs/current/reference/html/
@Slf4j
@Service
public class RawDataConsumer {

    @Getter
    private final List<RawData> rawDataList = new CopyOnWriteArrayList<>();

    @KafkaListener(topics = "${kafka.listener.topic}")
    public void listen(ConsumerRecord<String, RawData> record) {
        log.info("Received key: {}", record.key());
        rawDataList.add(record.value());
    }

}
