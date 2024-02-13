package com.example.micrometerboot.config;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
@EnableFeignClients(basePackages = {"com.example.micrometerboot.client"})
public class FeignConfig {

    // connect time out
    private static int connectTimeOutMillis = 3000;
    // read time out
    private static int readTimeOutMillis = 3000;
    // 각 시도간의 차이
    private static long period = 1000;
    // 모든 재시도 사이의 시간
    private static long maxPeriod = 1;
    // 최대 시도 수
    private static int maxAttempts = 5;

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeOutMillis, TimeUnit.SECONDS, readTimeOutMillis, TimeUnit.SECONDS, true);
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(2000, SECONDS.toMillis(5), 5);
    }

}
