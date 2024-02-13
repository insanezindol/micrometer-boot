package com.example.micrometerboot.redis.repository;

import com.example.micrometerboot.redis.entity.PersonEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("personRedisRepository")
public interface PersonRedisRepository extends CrudRepository<PersonEntity, String> {

}
