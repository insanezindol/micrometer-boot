package com.example.micrometerboot.mysql.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private Integer uid;

    @Column(name = "user_name")
    private String userName;

    @Version
    private int version;

    @Column(name = "reg_date")
    private Date regDate;

    @Column(name = "upd_date")
    private Date updDate;

    @Builder
    public UserInfo(Integer uid, String userName, int version, Date regDate, Date updDate) {
        this.uid = uid;
        this.userName = userName;
        this.version = version;
        this.regDate = regDate;
        this.updDate = updDate;
    }

}
