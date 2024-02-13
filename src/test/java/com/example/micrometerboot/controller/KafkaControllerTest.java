package com.example.micrometerboot.controller;

import com.example.micrometerboot.dto.UserInfoDto;
import com.example.micrometerboot.kafka.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.fixturemonkey.FixtureMonkey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KafkaController.class)
class KafkaControllerTest {

    static final ObjectMapper _MAPPER = new ObjectMapper();
    static final String _USER_NAME = "JINHYUNGLEE";

    @Autowired
    MockMvc mvc;

    @MockBean
    KafkaProducer kafkaProducer;

    FixtureMonkey fixtureMonkey;

    @BeforeEach
    void setup() {
        this.fixtureMonkey = FixtureMonkey.create();
    }

    @Test
    @DisplayName("카프카 publish")
    void publish() throws Exception {
        // given
        UserInfoDto userInfoDto = fixtureMonkey.giveMeOne(UserInfoDto.class);
        willDoNothing().given(kafkaProducer).sendMessage(any());

        // when, then
        mvc.perform(post("/kafka/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_MAPPER.writeValueAsString(userInfoDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("success")));
        verify(kafkaProducer, times(1)).sendMessage(any());
    }

}
