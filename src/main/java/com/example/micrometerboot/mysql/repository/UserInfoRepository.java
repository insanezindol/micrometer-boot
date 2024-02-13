package com.example.micrometerboot.mysql.repository;

import com.example.micrometerboot.mysql.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

    List<UserInfo> findByUserName(String userName);

}
