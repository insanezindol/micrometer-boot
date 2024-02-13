package com.example.micrometerboot.Integration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.example.micrometerboot.dto.UserInfoDto;
import com.example.micrometerboot.kafka.KafkaProducer;
import com.example.micrometerboot.log.MemoryAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:29092", "port=29092"})
@ActiveProfiles("test")
@SpringBootTest
class KafkaIntegrationTest {

    private static MemoryAppender memoryAppender;
    private static final String LOGGER_NAME = "com.example.micrometerboot.kafka";
    static final String _USER_NAME = "JINHYUNGLEE";

    @Autowired
    private KafkaProducer kafkaProducer;

    @BeforeEach
    void setup() {
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
    @DisplayName("kafka publish consume test")
    public void kafka_publish_consume_test() throws Exception {
        // given
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserName(_USER_NAME);

        // when
        kafkaProducer.sendMessage(userInfoDto);
        Thread.sleep(3000);

        // then
        assertEquals(3, memoryAppender.getSize());
        assertTrue(memoryAppender.contains("Produce message", Level.INFO));
        assertTrue(memoryAppender.contains("Consumed message", Level.INFO));
    }

}