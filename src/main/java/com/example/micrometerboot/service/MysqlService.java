package com.example.micrometerboot.service;

import com.example.micrometerboot.dto.UserInfoDto;
import com.example.micrometerboot.mysql.entity.Student;
import com.example.micrometerboot.mysql.entity.UserInfo;
import com.example.micrometerboot.mysql.repository.StudentRepository;
import com.example.micrometerboot.mysql.repository.UserInfoRepository;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MysqlService {

    private final UserInfoRepository userInfoRepository;

    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public List<UserInfo> findAllUser() {
        return userInfoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UserInfo findUserById(int id) {
        // micrometer
        String metricName = "mysql.find.user.by.id";
        List<Tag> tags = List.of(Tag.of("service_name", "MysqlService"), Tag.of("user_id", String.valueOf(id)));
        Metrics.counter(metricName, tags).increment();

        return userInfoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<UserInfo> findUserByUserName(String userName) {
        return userInfoRepository.findByUserName(userName);
    }

    @Transactional
    public UserInfo addUser(UserInfoDto userInfoDto) {
        UserInfo userInfo = UserInfo.builder()
                .userName(userInfoDto.getUserName())
                .regDate(new Date())
                .updDate(new Date())
                .build();
        return userInfoRepository.save(userInfo);
    }

    @Transactional
    public UserInfo modifyUser(int id, UserInfoDto userInfoDto) {
        UserInfo userInfo = userInfoRepository.findById(id).orElse(null);
        Optional.ofNullable(userInfo)
                .ifPresent(user -> {
                    user.setUserName(userInfoDto.getUserName());
                    user.setUpdDate(new Date());
                    userInfoRepository.save(user);
                });
        return userInfo;
    }

    @Transactional
    public void removeUser(int id) {
        UserInfo userInfo = userInfoRepository.findById(id).orElse(null);
        Optional.ofNullable(userInfo)
                .ifPresent(user -> {
                    userInfoRepository.delete(user);
                });
    }


    @Transactional(readOnly = true)
    public Student findStudent(long id) {
        log.info("findStudent : {}", id);
        return studentRepository.findStudentById(id);
    }

    @Transactional
    public Student addStudent(String name) {
        log.info("addStudent : {}", name);
        Student student = Student.builder()
                .name(name)
                .build();
        return studentRepository.save(student);
    }

    @Transactional
    @Retryable(interceptor = "optimisticLockingAutoRetryInterceptor")
    public Student modifyStudent(long id, String newName) {
        log.info("modifyStudent : {}, {}", id, newName);
        Student student = studentRepository.findStudentById(id);
        student.changeName(newName);
        return student;
    }

    @Transactional
    public void removeStudent(long id) {
        log.info("removeStudent : {}", id);
        studentRepository.deleteById(id);
    }

}
