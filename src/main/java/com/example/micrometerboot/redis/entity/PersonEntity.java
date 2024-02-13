package com.example.micrometerboot.redis.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@Setter
@RedisHash(value = "person", timeToLive = 600)
public class PersonEntity {

    @Id
    private String id;

    private String name;

    private Integer age;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
