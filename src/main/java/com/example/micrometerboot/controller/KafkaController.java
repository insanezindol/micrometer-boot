package com.example.micrometerboot.controller;

import com.example.micrometerboot.dto.ResponseDto;
import com.example.micrometerboot.dto.UserInfoDto;
import com.example.micrometerboot.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/kafka/publish")
    public ResponseDto publish(@RequestBody UserInfoDto userInfoDto) {
        ResponseDto response = new ResponseDto();
        kafkaProducer.sendMessage(userInfoDto);
        return response;
    }

}
