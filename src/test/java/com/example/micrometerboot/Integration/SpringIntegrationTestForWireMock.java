package com.example.micrometerboot.Integration;

import com.example.micrometerboot.client.MockFeignClient;
import com.example.micrometerboot.kafka.KafkaProducer;
import com.example.micrometerboot.service.ElasticsearchService;
import com.example.micrometerboot.service.MysqlService;
import com.example.micrometerboot.service.RedisService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.admin.model.ListStubMappingsResult;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;

@Slf4j
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringIntegrationTestForWireMock {

    @Autowired
    MysqlService mysqlService;

    @Autowired
    ElasticsearchService elasticsearchService;

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    MockFeignClient mockFeignClient;

    @Autowired
    RedisService redisService;

    @Container
    static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer(DockerImageName.parse("mysql:5.7.40"))
            .withInitScript("init-db.sql")
            .withDatabaseName("appdb")
            .withUsername("test")
            .withPassword("test")
            .withEnv("MYSQL_ROOT_PASSWORD", "test")
            .withReuse(true)
            .waitingFor(Wait.forListeningPort());

    @Container
    static ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:8.5.2")
            .withEnv("discovery.type", "single-node")
            .withEnv("node.name", "local-node")
            .withEnv("cluster.name", "local-cluster")
            .withEnv("xpack.security.enabled", "false")
            .withReuse(true)
            .waitingFor(Wait.forListeningPort());

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.0"))
            .withReuse(true)
            .waitingFor(Wait.forListeningPort());

    @Container
    static GenericContainer redisContainer = new GenericContainer(DockerImageName.parse("redis:7.0.7"))
            .withExposedPorts(6379)
            .withReuse(true)
            .waitingFor(Wait.forListeningPort());

    private static WireMockServer wireMockServer;


    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("elasticsearch.host", elasticsearchContainer::getHttpHostAddress);
        registry.add("spring.kafka.consumer.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("wiremock.port", () -> "8080");
        registry.add("spring.redis.port", () -> "" + redisContainer.getMappedPort(6379));
    }

    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
        elasticsearchContainer.start();
        kafkaContainer.start();
        redisContainer.start();
        // wiremock 시작
        wireMockServer = new WireMockServer(wireMockConfig().port(8080));
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
        elasticsearchContainer.stop();
        kafkaContainer.stop();
        redisContainer.stop();
        // wiremock 종료
        wireMockServer.stop();
    }

    @BeforeEach
    void resetWiremock() {
        wireMockServer.resetAll();

        ListStubMappingsResult listStubMappingsResult = wireMockServer.listAllStubMappings();
        for (StubMapping stubMapping : listStubMappingsResult.getMappings()){
            log.info("id : {}", stubMapping.getId());
            log.info("request : {}", stubMapping.getRequest());
            log.info("response : {}", stubMapping.getResponse());
        }
    }

    /**
     * Wiremock 정상 체크
     */
    @Test
    @Order(1)
    @DisplayName("check wiremock")
    void checkWiremockContainers() {
        log.info("baseUrl: {} ", wireMockServer.baseUrl());
        assertEquals("8080", String.valueOf(wireMockServer.port()));
    }

    /**
     * wiremock 테스트
     */

    @Test
    @Order(2)
    @DisplayName("sample1")
    void simpleStubTestingOne() {
        // given
        String responseBody = "1";
        String apiUrl = "/sample";
        stubFor(get(apiUrl).willReturn(ok(responseBody)));

        // when
        String apiResponse = getContent(wireMockServer.baseUrl() + apiUrl);

        // then
        assertEquals(apiResponse, responseBody);
        verify(getRequestedFor(urlEqualTo(apiUrl)));
    }

    @Test
    @Order(3)
    @DisplayName("sample2")
    void simpleStubTestingTwo() {
        // given
        String responseBody = "2";
        String apiUrl = "/sample";
        stubFor(get(apiUrl).willReturn(ok(responseBody)));

        // when
        String apiResponse = getContent(wireMockServer.baseUrl() + apiUrl);

        // then
        assertEquals(apiResponse, responseBody);
        verify(getRequestedFor(urlEqualTo(apiUrl)));
    }

    private String getContent(String url) {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        return testRestTemplate.getForObject(url, String.class);
    }

}
