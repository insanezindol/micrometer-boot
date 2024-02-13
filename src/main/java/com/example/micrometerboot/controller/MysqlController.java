package com.example.micrometerboot.controller;

import com.example.micrometerboot.dto.ResponseDto;
import com.example.micrometerboot.dto.StudentDto;
import com.example.micrometerboot.dto.UserInfoDto;
import com.example.micrometerboot.service.MysqlService;
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
public class MysqlController {

    private final MysqlService mysqlService;

    @GetMapping("/user")
    public ResponseDto findAllUser() {
        log.info("[BEG] findAllUser");
        ResponseDto response = new ResponseDto();
        response.setData(mysqlService.findAllUser());
        log.info("[END] findAllUser");
        return response;
    }

    @GetMapping("/user/id/{id}")
    public ResponseDto findUserById(@PathVariable("id") int id) {
        log.info("[BEG] findUserById");
        ResponseDto response = new ResponseDto();
        response.setData(mysqlService.findUserById(id));
        log.info("[END] findUserById");
        return response;
    }

    @GetMapping("/user/name/{userName}")
    public ResponseDto findUserByUserName(@PathVariable("userName") String userName) {
        log.info("[BEG] findUserByUserName");
        ResponseDto response = new ResponseDto();
        response.setData(mysqlService.findUserByUserName(userName));
        log.info("[END] findUserByUserName");
        return response;
    }

    @PostMapping("/user")
    public ResponseDto addUser(@RequestBody UserInfoDto userInfoDto) {
        log.info("[BEG] addUser");
        ResponseDto response = new ResponseDto();
        response.setData(mysqlService.addUser(userInfoDto));
        log.info("[END] addUser");
        return response;
    }

    @PutMapping("/user/{id}")
    public ResponseDto modifyUser(@PathVariable("id") int id, @RequestBody UserInfoDto userInfoDto) {
        log.info("[BEG] modifyUser");
        ResponseDto response = new ResponseDto();
        response.setData(mysqlService.modifyUser(id, userInfoDto));
        log.info("[END] modifyUser");
        return response;
    }

    @DeleteMapping("/user/{id}")
    public ResponseDto removeUser(@PathVariable("id") int id) {
        log.info("[BEG] removeUser");
        ResponseDto response = new ResponseDto();
        mysqlService.removeUser(id);
        log.info("[END] removeUser");
        return response;
    }




    @GetMapping("/student/{id}")
    public ResponseDto findStudent(@PathVariable("id") long id) {
        log.info("[BEG] findStudent");
        ResponseDto response = new ResponseDto();
        response.setData(mysqlService.findStudent(id));
        log.info("[END] findStudent");
        return response;
    }

    @PostMapping("/student")
    public ResponseDto addStudent(@RequestBody StudentDto studentDto) {
        log.info("[BEG] addStudent");
        ResponseDto response = new ResponseDto();
        response.setData(mysqlService.addStudent(studentDto.getName()));
        log.info("[END] addStudent");
        return response;
    }

    @PutMapping("/student/{id}")
    public ResponseDto modifyStudent(@PathVariable("id") long id, @RequestBody StudentDto studentDto) {
        log.info("[BEG] modifyStudent");
        ResponseDto response = new ResponseDto();
        response.setData(mysqlService.modifyStudent(id, studentDto.getName()));
        log.info("[END] modifyStudent");
        return response;
    }

    @DeleteMapping("/student/{id}")
    public ResponseDto removeStudent(@PathVariable("id") long id) {
        log.info("[BEG] removeStudent");
        ResponseDto response = new ResponseDto();
        mysqlService.removeStudent(id);
        log.info("[END] removeStudent");
        return response;
    }

}
