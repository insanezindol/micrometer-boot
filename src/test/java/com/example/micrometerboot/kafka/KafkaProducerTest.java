package com.example.micrometerboot.kafka;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.example.micrometerboot.dto.UserInfoDto;
import com.example.micrometerboot.kafka.KafkaProducer;
import com.example.micrometerboot.log.MemoryAppender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@Slf4j
@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    private static MemoryAppender memoryAppender;
    private static final String LOGGER_NAME = "com.example.micrometerboot.kafka";
    static final String _USER_NAME = "JINHYUNGLEE";

    KafkaProducer kafkaProducer;

    @Mock
    KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        this.kafkaProducer = new KafkaProducer(kafkaTemplate, mapper);
        Logger logger = (Logger) LoggerFactory.getLogger(LOGGER_NAME);
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.DEBUG);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    @AfterEach
    public void cleanUp() {
        memoryAppender.reset();
        memoryAppender.stop();
    }

    @Test
    @DisplayName("kafka publish exception test")
    public void kafka_publish_exception_test() throws Exception {
        // given
        doThrow(JsonProcessingException.class).when(mapper).writeValueAsString(any());
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserName(_USER_NAME);

        // when
        kafkaProducer.sendMessage(userInfoDto);

        // then
        assertEquals(1, memoryAppender.getSize());
        assertTrue(memoryAppender.contains("ObjectMapper Error", Level.ERROR));
    }

}