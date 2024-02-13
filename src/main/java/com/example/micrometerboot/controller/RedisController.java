package com.example.micrometerboot.controller;

import com.example.micrometerboot.dto.PersonDto;
import com.example.micrometerboot.dto.ResponseDto;
import com.example.micrometerboot.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    @GetMapping("/person/{id}")
    public ResponseDto findPersonById(@PathVariable("id") String id) {
        log.info("[BEG] findPersonById");
        ResponseDto response = new ResponseDto();
        response.setData(redisService.findPersonById(id));
        log.info("[END] findPersonById");
        return response;
    }

    @PostMapping("/person")
    public ResponseDto addPerson(@RequestBody PersonDto personDto) {
        log.info("[BEG] addPerson");
        ResponseDto response = new ResponseDto();
        response.setData(redisService.addPerson(personDto));
        log.info("[END] addPerson");
        return response;
    }

    @PutMapping("/person/{id}")
    public ResponseDto modifyPerson(@PathVariable("id") String id, @RequestBody PersonDto personDto) {
        log.info("[BEG] modifyPerson");
        ResponseDto response = new ResponseDto();
        response.setData(redisService.modifyPerson(id, personDto));
        log.info("[END] modifyPerson");
        return response;
    }

    @DeleteMapping("/person/{id}")
    public ResponseDto removePerson(@PathVariable("id") String id) {
        log.info("[BEG] removePerson");
        ResponseDto response = new ResponseDto();
        redisService.removePerson(id);
        log.info("[END] removePerson");
        return response;
    }

}
