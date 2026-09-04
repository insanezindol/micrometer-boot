![header](https://capsule-render.vercel.app/api?type=wave&color=auto&height=300&section=header&text=micrometer%20boot&fontSize=90)

# Micrometer Boot 📊

Spring Boot 애플리케이션에서 Micrometer를 활용한 모니터링 및 메트릭 수집 데모 프로젝트입니다.

## 🛠️ 기술 스택

-   **Java**: 21
-   **Spring Boot**: 3.5.16
-   **Spring Cloud**: 2025.0.3
-   **데이터베이스**: MySQL 8.0.43, Redis 7.0.7
-   **메시징**: Apache Kafka
-   **검색엔진**: Elasticsearch
-   **Monitoring**: Micrometer + Prometheus
-   **HTTP 클라이언트**: OpenFeign
-   **Test**: JUnit 5, Testcontainers, WireMock
-   **빌드 도구**: Maven

## 📋 주요 기능

### 🔍 모니터링 및 메트릭

-   **Micrometer**: 애플리케이션 메트릭 수집
-   **Prometheus**: 메트릭 저장 및 조회
-   **Spring Boot Actuator**: 헬스체크 및 운영 정보 제공

### 💾 데이터 저장소 통합

-   **MySQL**: 관계형 데이터베이스 연동
-   **Redis**: 캐시 및 세션 관리
-   **Elasticsearch**: 전문 검색 및 로그 분석

### 🚀 서비스 통신

-   **Apache Kafka**: 비동기 메시징
-   **OpenFeign**: HTTP 클라이언트 (서비스 간 통신)
-   **WireMock**: API 모킹 및 테스트

## 🏗️ 프로젝트 구조

```
src/
├── main/
│   ├── java/com/example/micrometerboot/
│   │   ├── MicrometerBootApplication.java    # 메인 애플리케이션
│   │   ├── client/                          # HTTP 클라이언트
│   │   │   └── MockFeignClient.java
│   │   ├── config/                          # 설정 클래스
│   │   │   ├── ElasticSearchConfig.java
│   │   │   ├── FeignConfig.java
│   │   │   ├── MDCLoggingFilter.java
│   │   │   └── RetryConfiguration.java
│   │   ├── controller/                      # REST API 컨트롤러
│   │   │   ├── AdsController.java
│   │   │   ├── ElasticsearchController.java
│   │   │   ├── FeignController.java
│   │   │   ├── KafkaController.java
│   │   │   ├── MysqlController.java
│   │   │   └── RedisController.java
│   │   ├── dto/                            # 데이터 전송 객체
│   │   ├── elasticsearch/                   # Elasticsearch 관련
│   │   ├── kafka/                          # Kafka 프로듀서/컨슈머
│   │   ├── mysql/                          # MySQL 엔티티/레포지토리
│   │   ├── redis/                          # Redis 엔티티/레포지토리
│   │   └── service/                        # 비즈니스 로직
│   └── resources/
│       ├── application.yml                  # 애플리케이션 설정
│       └── logback.xml                     # 로깅 설정
└── test/                                   # 테스트 코드
    ├── java/                              # 유닛/통합 테스트
    └── resources/
        ├── application-test.yml           # 테스트 환경 설정
        └── *.http                        # HTTP 요청 테스트 파일
```

## 🚀 빠른 시작

### 1. 전제 조건

-   Java 21 이상
-   Docker & Docker Compose
-   Maven 3.6 이상

### 2. 인프라 서비스 실행

```bash
# Docker Compose로 필수 서비스 실행 (MySQL, Redis, Kafka, Elasticsearch, Prometheus)
docker-compose up -d
```

### 3. 데이터베이스 초기화

MySQL 데이터베이스 설정:

```bash
# MySQL 컨테이너 접속
docker exec -it mysql mysql -uroot -p123456

# 데이터베이스 생성
CREATE DATABASE appdb;
CREATE USER 'appuser'@'%' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON appdb.* TO 'appuser'@'%';
FLUSH PRIVILEGES;
```

### 4. 애플리케이션 실행

```bash
# Maven을 사용한 실행
./mvnw spring-boot:run

# 또는 JAR 파일 빌드 후 실행
./mvnw clean package -DskipTests
java -jar target/micrometer-boot-0.0.1-SNAPSHOT.jar
```

### 5. 서비스 확인

애플리케이션 실행 후 다음 URL들을 확인할 수 있습니다:

-   **애플리케이션**: http://localhost:8080
-   **헬스체크**: http://localhost:8080/actuator/health
-   **메트릭**: http://localhost:8080/actuator/metrics
-   **Prometheus 메트릭**: http://localhost:8080/actuator/prometheus
-   **Prometheus UI**: http://localhost:9090
-   **WireMock**: http://localhost:9999

## 📚 API 엔드포인트

### MySQL 관련

-   `GET /mysql/students` - 학생 목록 조회
-   `POST /mysql/student` - 학생 생성
-   `PUT /mysql/student/{id}` - 학생 정보 수정
-   `DELETE /mysql/student/{id}` - 학생 삭제

### Redis 관련

-   `GET /redis/users` - 사용자 목록 조회
-   `POST /redis/user` - 사용자 생성
-   `GET /redis/user/{id}` - 사용자 조회
-   `DELETE /redis/user/{id}` - 사용자 삭제

### Kafka 관련

-   `POST /kafka/send` - 메시지 발송
-   `GET /kafka/receive` - 메시지 수신 확인

### Elasticsearch 관련

-   `POST /elasticsearch/blog` - 블로그 문서 생성
-   `GET /elasticsearch/blog/{id}` - 블로그 문서 조회
-   `GET /elasticsearch/blogs/search` - 블로그 검색

### Feign Client 관련

-   `GET /feign/test` - Feign 클라이언트 테스트
-   `GET /feign/users` - 외부 API 사용자 조회

## 🧪 테스트 실행

```bash
# 전체 테스트 실행
./mvnw test

# 특정 테스트 클래스 실행
./mvnw test -Dtest=MysqlControllerTest

# 통합 테스트 실행
./mvnw test -Dtest=SpringIntegrationTest
```

### 테스트 종류

-   **단위 테스트**: 개별 컴포넌트 테스트
-   **통합 테스트**: Testcontainers를 사용한 실제 데이터베이스 테스트
-   **WireMock 테스트**: HTTP 클라이언트 모킹 테스트
-   **Kafka 테스트**: 메시징 통합 테스트

## 📊 모니터링

### Micrometer 메트릭

-   **JVM 메트릭**: 메모리, GC, 스레드 정보
-   **HTTP 메트릭**: 요청 수, 응답 시간, 상태 코드
-   **데이터베이스 메트릭**: 커넥션 풀, 쿼리 실행 시간
-   **커스텀 메트릭**: 비즈니스 로직 관련 메트릭

### Prometheus 쿼리 예시

```promql
# HTTP 요청률
rate(http_server_requests_seconds_count[5m])

# JVM 힙 메모리 사용률
jvm_memory_used_bytes{area="heap"} / jvm_memory_max_bytes{area="heap"} * 100

# 데이터베이스 커넥션 수
hikaricp_connections_active{pool="HikariPool-1"}
```

## 🔧 설정

### 주요 설정 파일

-   `application.yml`: 메인 애플리케이션 설정
-   `application-test.yml`: 테스트 환경 설정
-   `docker-compose.yml`: 인프라 서비스 설정
-   `prometheus.yml`: Prometheus 설정
-   `logback.xml`: 로깅 설정

### 환경별 설정

```bash
# 개발 환경
java -jar app.jar --spring.profiles.active=dev

# 테스트 환경
java -jar app.jar --spring.profiles.active=test

# 운영 환경
java -jar app.jar --spring.profiles.active=prod
```

## 🐳 Docker

### 애플리케이션 Docker 이미지 빌드

```bash
# Dockerfile을 사용한 이미지 빌드
docker build -t micrometer-boot:latest .

# 컨테이너 실행
docker run -p 8080:8080 micrometer-boot:latest
```

## Code Coverage (jacoco)

![1](https://user-images.githubusercontent.com/32256571/206897162-4fdb8785-b8e4-4f62-9f79-a6bef7b8fc26.png)
