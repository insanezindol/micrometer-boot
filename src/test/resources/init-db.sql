use appdb;

CREATE TABLE `user_info`
(
    `uid`       int(11) NOT NULL AUTO_INCREMENT,
    `user_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `version`   int(11) DEFAULT NULL,
    `reg_date`  datetime                                DEFAULT CURRENT_TIMESTAMP,
    `upd_date`  datetime                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `student`
(
    `id`        int(11) NOT NULL AUTO_INCREMENT,
    `name`      varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `version`   int(11) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
