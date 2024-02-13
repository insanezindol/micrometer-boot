package com.example.micrometerboot.kafka;

import com.example.micrometerboot.dto.UserInfoDto;
import com.example.micrometerboot.mysql.entity.UserInfo;
import com.example.micrometerboot.service.MysqlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    static final ObjectMapper _MAPPER = new ObjectMapper();
    private final MysqlService mysqlService;

    // max.poll.records : poll ()에 대한 단일 호출에서 반환되는 최대 레코드 수 (Default:500)
    // max.poll.interval.ms : 컨슈머가 polling하고 commit 할때까지의 대기시간 (Default:300000)
    @KafkaListener(topics = "kafka-demo", groupId = "test-group-id", concurrency = "1")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            String message = record.value();
            log.info("Consumed message : {}", message);
            UserInfoDto userInfoDto = _MAPPER.readValue(message, UserInfoDto.class);
            UserInfo savedUser = mysqlService.addUser(userInfoDto);
            log.info("savedUser : {}", savedUser);
        } catch (Exception e) {
            log.error("ObjectMapper Error : ", e);
        }
    }

}
