![header](https://capsule-render.vercel.app/api?type=wave&color=auto&height=300&section=header&text=micrometer%20boot&fontSize=90)
# Springboot Skeleton API
* This is the springboot skeleton sample API.

![Java17](https://img.shields.io/badge/java17-%23ED8B00.svg?style=for-the-badge&logoColor=white)
![Spring Boot 2.7.7](https://img.shields.io/badge/springboot2.7.7-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

## Getting Started
* **The jdk version must be installed at least 17 versions**
* Cloning a repository
* Execute the maven clean and install command
```bash
mvn clean install
```
* Execute the docker compose command
```bash
docker-compose up -d
```
* Run 'MicrometerBootApplication'

## Library
### [pom.yml](https://github.com/insanezindol/micrometer-boot/blob/master/pom.xml)
* Spring Boot Starter Web
* Spring Boot Starter Actuator
* Spring Boot Starter Data Elasticsearch
* Spring Boot Starter Data JPA
* Spring Boot Starter Test
* Spring Kafka Support
* Spring Cloud Starter OpenFeign
* MySQL Connector/J
* Project Lombok
* Guava: Google Core Libraries For Java
* Micrometer Core
* Micrometer Registry Prometheus
* Testcontainers Core
* Testcontainers :: JUnit Jupiter Extension
* Testcontainers :: JDBC :: MySQL
* Testcontainers :: Kafka
* TestContainers :: Elasticsearch
* Fixture Monkey

## Docker
### [docker-compose.yml](https://github.com/insanezindol/micrometer-boot/blob/master/docker-compose.yml)
`docker-compose up -d`
#### mysql
* [image : mysql:5.7.40](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/resources/init-db.sql)
#### elasticsearch
* image : elasticsearch:8.5.2
#### kibana
* image : kibana:8.5.2
#### zookeeper
* image : confluentinc/cp-zookeeper:7.3.0
#### kafka
* image : confluentinc/cp-kafka:7.3.0
#### kafka-manager
* image : hlebalbau/kafka-manager:stable
#### prometheus
* image : prom/prometheus:latest
#### grafana
* image : grafana/grafana:latest
#### wiremock
* [image : wiremock/wiremock:2.35.0](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/resources/mappings/test-api.json)
  * `GET /v1/get-test`
  * `GET /v1/get-test/test1234`
  * `POST /v1/post-test`

## API Specification
### [MysqlController](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/resources/mysql-controller.http)
* `GET /user`
* `GET /user/id/{id}`
* `GET /user/name/{name}`
* `POST /user`
* `PUT /user/{id}`
* `DELETE /user/{id}`
### [ElasticsearchController](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/resources/elastic-controller.http)
* `GET /blog`
* `GET /blog/id/{id}`
* `GET /blog/title/{title}`
* `POST /blog`
* `PUT /blog/{id}`
* `DELETE /blog/{id}`
### [KafkaController](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/resources/kafka-controller.http)
* `POST /kafka/publish`
### [FeignController](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/resources/feign-controller.http)
* `GET /product`
* `GET /product/id/{id}`
* `POST /product`

## Testing
### ServiceTest
* [MysqlServiceTest](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/java/com/example/micrometerboot/service/MysqlServiceTest.java)
* [ElasticsearchServiceTest](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/java/com/example/micrometerboot/service/ElasticsearchServiceTest.java)
* [KafkaTest](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/java/com/example/micrometerboot/kafka/KafkaTest.java)
### WebMvcTest
* [MysqlControllerTest](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/java/com/example/micrometerboot/controller/MysqlControllerTest.java)
* [ElasticsearchControllerTest](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/java/com/example/micrometerboot/controller/ElasticsearchControllerTest.java)
* [KafkaControllerTest](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/java/com/example/micrometerboot/controller/KafkaControllerTest.java)
* [FeignControllerTest](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/java/com/example/micrometerboot/controller/FeignControllerTest.java)
### IntegrationTest
* [SpringIntegrationTest](https://github.com/insanezindol/micrometer-boot/blob/master/src/test/java/com/example/micrometerboot/Integration/SpringIntegrationTest.java)

## Code Coverage (jacoco)
![1](https://user-images.githubusercontent.com/32256571/206897162-4fdb8785-b8e4-4f62-9f79-a6bef7b8fc26.png)
