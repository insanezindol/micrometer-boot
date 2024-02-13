package com.example.micrometerboot.service;

import com.example.micrometerboot.dto.PersonDto;
import com.example.micrometerboot.redis.entity.PersonEntity;
import com.example.micrometerboot.redis.repository.PersonRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final PersonRedisRepository personRedisRepository;

    public PersonEntity findPersonById(String id) {
        return personRedisRepository.findById(id).orElse(null);
    }

    public PersonEntity addPerson(PersonDto personDto) {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setName(personDto.getName());
        personEntity.setAge(personDto.getAge());
        personEntity.setCreatedAt(LocalDateTime.now());
        personEntity.setUpdatedAt(LocalDateTime.now());
        return personRedisRepository.save(personEntity);
    }

    public PersonEntity modifyPerson(String id, PersonDto personDto) {
        PersonEntity personEntity = personRedisRepository.findById(id).orElse(null);
        Optional.ofNullable(personEntity)
                .ifPresent(person -> {
                    person.setName(personDto.getName());
                    person.setAge(personDto.getAge());
                    person.setUpdatedAt(LocalDateTime.now());
                    personRedisRepository.save(person);
                });
        return personEntity;
    }

    public void removePerson(String id) {
        PersonEntity personEntity = personRedisRepository.findById(id).orElse(null);
        Optional.ofNullable(personEntity)
                .ifPresent(person -> {
                    personRedisRepository.delete(person);
                });
    }

}
