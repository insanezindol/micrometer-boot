package com.example.micrometerboot.service;

import com.example.micrometerboot.dto.PersonDto;
import com.example.micrometerboot.mysql.entity.UserInfo;
import com.example.micrometerboot.redis.entity.PersonEntity;
import com.example.micrometerboot.redis.repository.PersonRedisRepository;
import com.navercorp.fixturemonkey.FixtureMonkey;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    static final String _ID = "TEST-ID";
    static final String _NAME = "JINHYUNGLEE";
    static final int _AGE = 100;

    RedisService redisService;

    FixtureMonkey fixtureMonkey;

    @Mock
    PersonRedisRepository personRedisRepository;

    @BeforeEach
    void setup() {
        this.redisService = new RedisService(personRedisRepository);
        this.fixtureMonkey = FixtureMonkey.create();
    }

    @Test
    @DisplayName("사용자 아이디 검색")
    void findPersonById() {
        // given
        PersonEntity personEntity = fixtureMonkey.giveMeBuilder(PersonEntity.class)
                .set(javaGetter(PersonEntity::getId), _ID)
                .set(javaGetter(PersonEntity::getName), _NAME)
                .set(javaGetter(PersonEntity::getAge), _AGE)
                .sample();
        when(personRedisRepository.findById(any())).thenReturn(Optional.of(personEntity));

        // when
        PersonEntity person = redisService.findPersonById(_ID);

        // then
        Assertions.assertNotNull(person);
        Assertions.assertEquals(_NAME, person.getName());
        verify(personRedisRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("사용자 추가")
    void addPerson() {
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
        when(personRedisRepository.save(any())).thenReturn(personEntity);

        // when
        PersonEntity savedPerson = redisService.addPerson(personDto);

        // then
        Assertions.assertNotNull(savedPerson);
        Assertions.assertEquals(personEntity.getName(), savedPerson.getName());
        Assertions.assertEquals(personEntity.getAge(), savedPerson.getAge());
        verify(personRedisRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("사용자 수정")
    void modifyPerson() {
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
        when(personRedisRepository.findById(any())).thenReturn(Optional.of(personEntity));

        // when
        PersonEntity savedPerson = redisService.modifyPerson(_ID, personDto);

        // then
        Assertions.assertNotNull(savedPerson);
        Assertions.assertEquals(name, savedPerson.getName());
        verify(personRedisRepository, times(1)).findById(any());
        verify(personRedisRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("사용자 삭제")
    void removePerson() {
        // given
        PersonEntity personEntity = fixtureMonkey.giveMeOne(PersonEntity.class);
        when(personRedisRepository.findById(any())).thenReturn(Optional.of(personEntity));

        // when
        redisService.removePerson(_ID);

        // then
        verify(personRedisRepository, times(1)).findById(any());
        verify(personRedisRepository, times(1)).delete(any());
    }

}
