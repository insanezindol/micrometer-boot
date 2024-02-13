package com.example.micrometerboot.kafka;

import com.example.micrometerboot.dto.UserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private static final String _TOPIC = "kafka-demo";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper _MAPPER;

    public void sendMessage(UserInfoDto userInfoDto) {
        try {
            String message = _MAPPER.writeValueAsString(userInfoDto);
            log.info("Produce message : {} : {}", _TOPIC, message);
            this.kafkaTemplate.send(_TOPIC, message);
        } catch (JsonProcessingException e) {
            log.error("ObjectMapper Error : ", e);
        }
    }

}
