package com.example.micrometerboot.Integration;

import com.example.micrometerboot.client.MockFeignClient;
import com.example.micrometerboot.dto.*;
import com.example.micrometerboot.elasticsearch.document.BlogDocument;
import com.example.micrometerboot.kafka.KafkaProducer;
import com.example.micrometerboot.mysql.entity.UserInfo;
import com.example.micrometerboot.redis.entity.PersonEntity;
import com.example.micrometerboot.service.ElasticsearchService;
import com.example.micrometerboot.service.MysqlService;
import com.example.micrometerboot.service.RedisService;
import com.navercorp.fixturemonkey.FixtureMonkey;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@Slf4j
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpringIntegrationTest {

    // mysql variable
    static final int _UID = 1;
    static final String _USER_NAME = "JINHYUNGLEE";

    // elasticsearch variable
    static String _ES_ID_1 = "";
    static String _ES_ID_2 = "";
    static final String _TITLE = "title";
    static final String _CONTENTS = "contents";

    // wiremock variable
    static final String _ORDER_ID = "ORD-11111";
    static final String _USER_ID = "user11111";
    static final String _PRODUCT_ID = "SHOES11111";
    static final String _PRODUCT_NAME = "NIKE SHOES";
    static final String _REQUESTED_AT = "2022-12-07T00:00:00.000Z";
    static final boolean _CACELED = false;
    static final int _FEE = 3000;

    // redis variable
    static String _REDIS_ID = "";
    static final String _NAME = "JINHYUNGLEE";
    static final int _AGE = 30;

    @Value("${wiremock.api-key}")
    private String _API_KEY;

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
    static GenericContainer wiremockContainer = new GenericContainer(DockerImageName.parse("wiremock/wiremock:2.35.0"))
            .withExposedPorts(8080)
            .withClasspathResourceMapping("./mappings", "/home/wiremock/mappings", BindMode.READ_ONLY)
            .withReuse(true)
            .waitingFor(Wait.forListeningPort());

    @Container
    static GenericContainer redisContainer = new GenericContainer(DockerImageName.parse("redis:7.0.7"))
            .withExposedPorts(6379)
            .withReuse(true)
            .waitingFor(Wait.forListeningPort());

    static FixtureMonkey fixtureMonkey;

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("elasticsearch.host", elasticsearchContainer::getHttpHostAddress);
        registry.add("spring.kafka.consumer.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("wiremock.port", () -> "" + wiremockContainer.getMappedPort(8080));
        registry.add("spring.redis.port", () -> "" + redisContainer.getMappedPort(6379));
    }

    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
        elasticsearchContainer.start();
        kafkaContainer.start();
        wiremockContainer.start();
        redisContainer.start();
        fixtureMonkey = FixtureMonkey.create();
    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
        elasticsearchContainer.stop();
        kafkaContainer.stop();
        wiremockContainer.stop();
        redisContainer.stop();
    }

    /**
     * TestContainer 정상 체크
     */

    @Test
    @Order(1)
    @DisplayName("check mysql containers")
    void checkMysqlContainers() {
        log.info("getJdbcDriverInstance: {} ", mySQLContainer.getJdbcDriverInstance());
        log.info("getJdbcUrl: {} ", mySQLContainer.getJdbcUrl());
        log.info("getMappedPort: {} ", mySQLContainer.getMappedPort(3306));
        log.info("getHost: {} ", mySQLContainer.getHost());
        log.info("getUsername: {} ", mySQLContainer.getUsername());
        log.info("getPassword: {} ", mySQLContainer.getPassword());
        assertEquals("localhost", mySQLContainer.getHost());
    }

    @Test
    @Order(2)
    @DisplayName("check elasticsearch containers")
    void checkElasticsearchContainers() {
        log.info("getImage: {} ", elasticsearchContainer.getImage());
        log.info("getHost: {} ", elasticsearchContainer.getHost());
        log.info("getHttpHostAddress: {} ", elasticsearchContainer.getHttpHostAddress());
        log.info("getEnv: {} ", elasticsearchContainer.getEnv());
        assertEquals("localhost", elasticsearchContainer.getHost());
    }


    @Test
    @Order(3)
    @DisplayName("check kafka containers")
    void checkKafkaContainers() {
        log.info("getImage: {} ", kafkaContainer.getImage());
        log.info("getHost: {} ", kafkaContainer.getHost());
        log.info("getBootstrapServers: {} ", kafkaContainer.getBootstrapServers());
        log.info("getEnv: {} ", kafkaContainer.getEnv());
        assertEquals("localhost", kafkaContainer.getHost());
    }

    @Test
    @Order(4)
    @DisplayName("check wiremock containers")
    void checkWiremockContainers() {
        log.info("getImage: {} ", wiremockContainer.getImage());
        log.info("getHost: {} ", wiremockContainer.getHost());
        log.info("getMappedPort: {} ", wiremockContainer.getMappedPort(8080));
        log.info("getEnv: {} ", wiremockContainer.getEnv());
        assertEquals("localhost", wiremockContainer.getHost());
    }

    @Test
    @Order(5)
    @DisplayName("check redis containers")
    void checkRedisContainers() {
        log.info("getImage: {} ", redisContainer.getImage());
        log.info("getHost: {} ", redisContainer.getHost());
        log.info("getMappedPort: {} ", redisContainer.getMappedPort(6379));
        assertEquals("localhost", redisContainer.getHost());
    }

    /**
     * MySQL 테스트
     */

    @Test
    @Order(10)
    @DisplayName("사용자 추가")
    void addUser() {
        // given
        UserInfoDto userInfoDto = buildUserInfoDto();

        // when
        UserInfo user = mysqlService.addUser(userInfoDto);

        // then
        Assertions.assertNotNull(user);
        Assertions.assertEquals(userInfoDto.getUserName(), user.getUserName());
    }

    @Test
    @Order(11)
    @DisplayName("사용자 전체 검색")
    void findAllUser() {
        // when
        List<UserInfo> list = mysqlService.findAllUser();

        // then
        Assertions.assertNotNull(list);
        Assertions.assertEquals(1, list.size());
        assertThat(list).extracting("uid", "userName").contains(tuple(_UID, _USER_NAME));
    }

    @Test
    @Order(12)
    @DisplayName("사용자 아이디 검색")
    void findUserById() {
        // when
        UserInfo user = mysqlService.findUserById(_UID);

        // then
        Assertions.assertNotNull(user);
        Assertions.assertEquals(_USER_NAME, user.getUserName());
    }

    @Test
    @Order(13)
    @DisplayName("사용자 이름 검색")
    void findUserByUserName() {
        // when
        List<UserInfo> list = mysqlService.findUserByUserName(_USER_NAME);

        // then
        Assertions.assertNotNull(list);
        Assertions.assertEquals(1, list.size());
        assertThat(list).extracting("uid", "userName").contains(tuple(_UID, _USER_NAME));
    }

    @Test
    @Order(14)
    @DisplayName("사용자 수정")
    void modifyUser() {
        // given
        final String modifyName = "DEAN";
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserName(modifyName);

        // when
        UserInfo savedUser = mysqlService.modifyUser(_UID, userInfoDto);

        // then
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(modifyName, savedUser.getUserName());
    }

    @Test
    @Order(15)
    @DisplayName("사용자 삭제")
    void removeUser() {
        // when
        mysqlService.removeUser(_UID);
        List<UserInfo> list = mysqlService.findAllUser();

        // then
        assertEquals(0, list.size());
    }

    /**
     * elasticsearch 테스트
     */

    @Test
    @Order(20)
    @DisplayName("블로그 추가")
    void addBlog() {
        // given, when
        BlogDocument blog1 = elasticsearchService.addBlog(buildBlogDto());
        BlogDocument blog2 = elasticsearchService.addBlog(buildBlogDto());
        _ES_ID_1 = blog1.getId();
        _ES_ID_2 = blog2.getId();

        // then
        Assertions.assertNotNull(blog1.getId());
        Assertions.assertNotNull(blog2.getId());
        Assertions.assertNotNull(blog1.getLogDate());
        Assertions.assertNotNull(blog2.getLogDate());
        Assertions.assertEquals(1, blog1.getVersion());
        Assertions.assertEquals(1, blog2.getVersion());
    }


    @Test
    @Order(21)
    @DisplayName("블로그 전체 검색")
    void findAllBlog() {
        // when
        List<BlogDocument> list = elasticsearchService.findAllBlog();

        // then
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.size());
        assertThat(list).extracting("id", "title", "contents")
                .contains(tuple(_ES_ID_1, _TITLE, _CONTENTS), tuple(_ES_ID_2, _TITLE, _CONTENTS));
    }

    @Test
    @Order(22)
    @DisplayName("블로그 아이디 검색")
    void findBlogById() {
        // when
        BlogDocument blog = elasticsearchService.findBlogById(_ES_ID_1);

        // then
        Assertions.assertNotNull(blog);
        Assertions.assertEquals(_ES_ID_1, blog.getId());
    }

    @Test
    @Order(23)
    @DisplayName("블로그 제목 검색")
    void findBlogByTitle() {
        // when
        List<BlogDocument> list = elasticsearchService.findBlogByTitle(_TITLE);

        // then
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.size());
        assertThat(list).extracting("id", "title", "contents")
                .contains(tuple(_ES_ID_1, _TITLE, _CONTENTS), tuple(_ES_ID_2, _TITLE, _CONTENTS));
    }

    @Test
    @Order(24)
    @DisplayName("블로그 수정")
    void modifyBlog() {
        // given
        final String modifyTitle = "modify title";
        final String modifyContents = "modify contents";
        BlogDto blogDto = new BlogDto();
        blogDto.setTitle(modifyTitle);
        blogDto.setContents(modifyContents);

        // when
        BlogDocument blog = elasticsearchService.modifyBlog(_ES_ID_1, blogDto);

        // then
        Assertions.assertNotNull(blog);
        Assertions.assertEquals(blogDto.getTitle(), blog.getTitle());
        Assertions.assertEquals(blogDto.getContents(), blog.getContents());
    }

    @Test
    @Order(25)
    @DisplayName("블로그 삭제")
    void removeBlog() {
        // when
        elasticsearchService.removeBlog(_ES_ID_1);
        elasticsearchService.removeBlog(_ES_ID_2);
        List<BlogDocument> list = elasticsearchService.findAllBlog();

        // then
        assertEquals(0, list.size());
    }

    /**
     * kafka 테스트
     */
    @Test
    @Order(30)
    @DisplayName("카프카 publish")
    void publish() throws Exception {
        // when
        kafkaProducer.sendMessage(buildUserInfoDto());
        TimeUnit.SECONDS.sleep(2);

        // when
        List<UserInfo> list = mysqlService.findAllUser();

        // then
        Assertions.assertNotNull(list);
        Assertions.assertEquals(1, list.size());
        assertThat(list).extracting("userName").contains(_USER_NAME);
    }


    /**
     * wiremock 테스트
     */
    @Test
    @Order(40)
    @DisplayName("상품 등록")
    void addProduct() {
        // when
        ResponseDto responseDto = mockFeignClient.addProduct(_API_KEY, buildProductDto());

        // then
        Assertions.assertNotNull(responseDto);
        Assertions.assertEquals(0, responseDto.getCode());
        Assertions.assertEquals("OK", responseDto.getMessage());
        Assertions.assertEquals("success", responseDto.getData());
    }

    @Test
    @Order(41)
    @DisplayName("상품 전체 검색")
    void findAllProduct() {
        // when
        List<ProductDto> list = mockFeignClient.findAllProduct(_API_KEY);

        // then
        Assertions.assertNotNull(list);
        Assertions.assertEquals(2, list.size());
        assertThat(list).extracting("orderId").contains("ORD-11111", "ORD-22222");
        assertThat(list).extracting("userId").contains("user11111", "user22222");
        assertThat(list).extracting("productId").contains("SHOES11111", "SHOES22222");
        assertThat(list).extracting("productName").contains("NIKE SHOES", "ADIDAS SHOES");
        assertThat(list).extracting("canceled", "fee").contains(tuple(_CACELED, _FEE));
    }

    @Test
    @Order(42)
    @DisplayName("상품 아이디 검색")
    void findProductById() {
        // when
        ProductDto product = mockFeignClient.findProduct(_API_KEY, _ORDER_ID);

        // then
        Assertions.assertNotNull(product);
        Assertions.assertEquals(_ORDER_ID, product.getOrderId());
        Assertions.assertEquals(_USER_ID, product.getUserId());
        Assertions.assertEquals(_PRODUCT_ID, product.getProductId());
        Assertions.assertEquals(_PRODUCT_NAME, product.getProductName());
    }

    /**
     * Redis 테스트
     */

    @Test
    @Order(50)
    @DisplayName("사람 추가")
    void addPerson() {
        // given
        PersonDto personDto = buildPersonDto();

        // when
        PersonEntity person = redisService.addPerson(personDto);
        _REDIS_ID = person.getId();

        // then
        Assertions.assertNotNull(person);
        Assertions.assertEquals(personDto.getName(), person.getName());
    }

    @Test
    @Order(51)
    @DisplayName("사람 아이디 검색")
    void findPersonById() {
        // when
        PersonEntity person = redisService.findPersonById(_REDIS_ID);

        // then
        Assertions.assertNotNull(person);
        Assertions.assertEquals(_NAME, person.getName());
    }

    @Test
    @Order(52)
    @DisplayName("사람 수정")
    void modifyPerson() {
        // given
        final String modifyName = "DEAN";
        final int modifyAge = 10;
        PersonDto personDto = new PersonDto();
        personDto.setName(modifyName);
        personDto.setAge(modifyAge);

        // when
        PersonEntity person = redisService.modifyPerson(_REDIS_ID, personDto);

        // then
        Assertions.assertNotNull(person);
        Assertions.assertEquals(modifyName, person.getName());
    }

    @Test
    @Order(53)
    @DisplayName("사람 삭제")
    void removePerson() {
        // when
        redisService.removePerson(_REDIS_ID);
        PersonEntity person = redisService.findPersonById(_REDIS_ID);

        // then
        assertNull(person);
    }

    static UserInfoDto buildUserInfoDto() {
        return fixtureMonkey.giveMeBuilder(UserInfoDto.class)
                .set(javaGetter(UserInfoDto::getUserName), _USER_NAME)
                .sample();
    }

    static BlogDto buildBlogDto() {
        return fixtureMonkey.giveMeBuilder(BlogDto.class)
                .set(javaGetter(BlogDto::getTitle), _TITLE)
                .set(javaGetter(BlogDto::getContents), _CONTENTS)
                .sample();
    }

    static ProductDto buildProductDto() {
        return fixtureMonkey.giveMeBuilder(ProductDto.class)
                .set(javaGetter(ProductDto::getOrderId), _ORDER_ID)
                .set(javaGetter(ProductDto::getUserId), _USER_ID)
                .set(javaGetter(ProductDto::getProductId), _PRODUCT_ID)
                .set(javaGetter(ProductDto::getProductName), _PRODUCT_NAME)
                .set(javaGetter(ProductDto::getRequestedAt), _REQUESTED_AT)
                .set(javaGetter(ProductDto::isCanceled), _CACELED)
                .set(javaGetter(ProductDto::getFee), _FEE)
                .sample();
    }

    static PersonDto buildPersonDto() {
        return fixtureMonkey.giveMeBuilder(PersonDto.class)
                .set(javaGetter(PersonDto::getName), _NAME)
                .set(javaGetter(PersonDto::getAge), _AGE)
                .sample();
    }

}
