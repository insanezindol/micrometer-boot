package com.example.micrometerboot.controller;

import com.example.micrometerboot.dto.PersonDto;
import com.example.micrometerboot.redis.entity.PersonEntity;
import com.example.micrometerboot.service.RedisService;
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

import java.time.LocalDateTime;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RedisController.class)
class RedisControllerTest {

    static final String _ID = "TEST-ID";
    static final String _NAME = "JINHYUNGLEE";
    static final int _AGE = 100;
    static final ObjectMapper _MAPPER = new ObjectMapper();

    @Autowired
    MockMvc mvc;

    @MockBean
    RedisService redisService;

    FixtureMonkey fixtureMonkey;

    @BeforeEach
    void setup() {
        this.fixtureMonkey = FixtureMonkey.create();
    }

    @Test
    @DisplayName("사용자 아이디 검색")
    void findUserById() throws Exception {
        // given
        PersonEntity personEntity = fixtureMonkey.giveMeBuilder(PersonEntity.class)
                .set(javaGetter(PersonEntity::getId), _ID)
                .set(javaGetter(PersonEntity::getName), _NAME)
                .set(javaGetter(PersonEntity::getAge), _AGE)
                .sample();
        given(redisService.findPersonById(any())).willReturn(personEntity);

        // when, then
        mvc.perform(get("/person/{id}", _ID))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(_NAME)));
        verify(redisService, times(1)).findPersonById(any());
    }

    @Test
    @DisplayName("사용자 추가")
    void addUser() throws Exception {
        // given
        PersonEntity personEntity = fixtureMonkey.giveMeBuilder(PersonEntity.class)
                .set(javaGetter(PersonEntity::getId), _ID)
                .set(javaGetter(PersonEntity::getName), _NAME)
                .set(javaGetter(PersonEntity::getAge), _AGE)
                .sample();
        PersonDto personDto = fixtureMonkey.giveMeBuilder(PersonDto.class)
                .set(javaGetter(PersonEntity::getName), _NAME)
                .set(javaGetter(PersonEntity::getAge), _AGE)
                .sample();
        given(redisService.addPerson(any())).willReturn(personEntity);

        // when, then
        mvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(_MAPPER.writeValueAsString(personDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(_NAME)));
        verify(redisService, times(1)).addPerson(any());
    }


    @Test
    @DisplayName("사용자 수정")
    void modifyUser() throws Exception {
        // given
        final String name = "USER2";
        final int age = 10;
        PersonEntity personEntity = fixtureMonkey.giveMeBuilder(PersonEntity.class)
                .set(javaGetter(PersonEntity::getId), _ID)
                .set(javaGetter(PersonEntity::getName), _NAME)
                .set(javaGetter(PersonEntity::getAge), _AGE)
                .sample();
        PersonDto personDto = fixtureMonkey.giveMeBuilder(PersonDto.class)
                .set(javaGetter(PersonEntity::getName), name)
                .set(javaGetter(PersonEntity::getAge), age)
                .sample();

        given(redisService.modifyPerson(any(), any())).willReturn(personEntity);

        // when, then
        mvc.perform(put("/person/{id}", _ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(_MAPPER.writeValueAsString(personDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(_NAME)));
        verify(redisService, times(1)).modifyPerson(any(), any());
    }

    @Test
    @DisplayName("사용자 삭제")
    void removeUser() throws Exception {
        // given
        willDoNothing().given(redisService).removePerson(any());

        // when, then
        mvc.perform(delete("/person/{id}", _ID))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("success")));
        verify(redisService, times(1)).removePerson(any());
    }

}
