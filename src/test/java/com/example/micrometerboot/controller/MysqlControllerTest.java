package com.example.micrometerboot.controller;

import com.example.micrometerboot.dto.UserInfoDto;
import com.example.micrometerboot.mysql.entity.UserInfo;
import com.example.micrometerboot.service.MysqlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.fixturemonkey.FixtureMonkey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MysqlController.class)
class MysqlControllerTest {

    /**
     * @WebMvcTest(테스트할컨트롤러.class) 해당 클래스만 실제로 로드하여 테스트를 해줍니다.
     * 아규먼트로 컨트롤러를 지정해주지 않으면 @Controller @RestController @ControllerAdvice 등등 컨트롤러와 연관된 bean들이 로드됩니다.
     * 스프링의 모든 빈을 로드하여 테스트하는 방식인 @SpringBootTest어노테이션 대신 컨트롤러 관련 코드만 테스트하고자 할때 사용하는 어노테이션입니다.
     * @Autowired MockMvc mvc;
     * 컨트롤러의 api를 테스트하는 용도인 MockMvc 객체를 주입받습니다.
     * perform(httpMethod)로 실행하며 andExpect, andDo, andReturn등으로 동작을 확인하는 방식입니다.
     * @MockBean MysqlService mysqlService;
     * MysqlController는 MysqlService를 스프링컨테이너에서 주입받고있으므로
     * 가짜 객체를 만들어 컨테이너가 주입할 수 있도록 해줍니다.
     * 해당객체는 가짜객체이므로 실제 행위를 하는 객체가 아닙니다.
     * 해당 객체 내부에서 의존하는 객체와 메서드들은 모두 가짜이며 실패하지만 않을뿐 기존에 정해진 동작을 수행하지 하지 않습니다.
     * <p>
     * given(mysqlService.findAllUser()).willReturn(Arrays.asList(userInfoEntity1, userInfoEntity2));
     * 가짜객체가 원하는 행위를 할 수 있도록 정의해줍니다.(given when 등을 사용합니다.)
     * memberService의 list() 메서드를 실행시키면 members를 리턴해달라는 요청입니다.
     * <p>
     * andExpect(content().string(containsString("JINHYUNGLEE")));
     * 리턴받은 body에 JINHYUNGLEE이라는 문자열이 존재하는지를 확인합니다.
     * given을 통해 mock객체의 예상한 행위가 정상적으로 동작했는지를 확인합니다.
     * <p>
     * verify(mysqlService).findAllUser();
     * 해당 메서드가 실행됐는지를 검증해줍니다.
     */

    static final int _ID1 = 1;
    static final int _ID2 = 2;
    static final String _USER_NAME = "JINHYUNGLEE";
    static final String _CHANGED_USER_NAME = "LEEJINHYUNG";
    static final ObjectMapper _MAPPER = new ObjectMapper();

    @Autowired
    MockMvc mvc;

    @MockBean
    MysqlService mysqlService;

    FixtureMonkey fixtureMonkey;

    @BeforeEach
    void setup() {
        this.fixtureMonkey = FixtureMonkey.create();
    }

    @Test
    @DisplayName("사용자 전체 검색")
    void findAllUser() throws Exception {
        // given
        final int docSize = 5;
        List<UserInfo> userInfos = fixtureMonkey.giveMe(UserInfo.class, docSize);

        given(mysqlService.findAllUser()).willReturn(userInfos);

        // when, then
        mvc.perform(get("/user"))
                .andExpect(status().isOk());
        verify(mysqlService, times(1)).findAllUser();
    }

    @Test
    @DisplayName("사용자 아이디 검색")
    void findUserById() throws Exception {
        // given
        UserInfo userInfo = fixtureMonkey.giveMeBuilder(UserInfo.class)
                .set(javaGetter(UserInfo::getUid), _ID1)
                .sample();
        given(mysqlService.findUserById(anyInt())).willReturn(userInfo);

        // when, then
        mvc.perform(get("/user/id/{id}", _ID1))
                .andExpect(status().isOk());
        verify(mysqlService, times(1)).findUserById(anyInt());
    }

    @Test
    @DisplayName("사용자 이름 검색")
    void findUserByUserName() throws Exception {
        // given
        UserInfo userInfo1 = fixtureMonkey.giveMeBuilder(UserInfo.class)
                .set(javaGetter(UserInfo::getUid), _ID1)
                .set(javaGetter(UserInfo::getUserName), _USER_NAME)
                .sample();
        UserInfo userInfo2 = fixtureMonkey.giveMeBuilder(UserInfo.class)
                .set(javaGetter(UserInfo::getUid), _ID2)
                .set(javaGetter(UserInfo::getUserName), _USER_NAME)
                .sample();
        given(mysqlService.findUserByUserName(any())).willReturn(Arrays.asList(userInfo1, userInfo2));

        // when, then
        mvc.perform(get("/user/name/{userName}", _USER_NAME))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(_USER_NAME)));
        verify(mysqlService, times(1)).findUserByUserName(any());
    }

    @Test
    @DisplayName("사용자 추가")
    void addUser() throws Exception {
        UserInfo userInfo = fixtureMonkey.giveMeBuilder(UserInfo.class)
                .set(javaGetter(UserInfo::getUid), _ID1)
                .set(javaGetter(UserInfo::getUserName), _USER_NAME)
                .sample();
        UserInfoDto userInfoDto = fixtureMonkey.giveMeBuilder(UserInfoDto.class)
                .set(javaGetter(UserInfoDto::getUserName), _USER_NAME)
                .sample();
        given(mysqlService.addUser(any())).willReturn(userInfo);

        mvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_MAPPER.writeValueAsString(userInfoDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(_USER_NAME)));
        verify(mysqlService, times(1)).addUser(any());
    }


    @Test
    @DisplayName("사용자 수정")
    void modifyUser() throws Exception {
        UserInfo userInfo = fixtureMonkey.giveMeBuilder(UserInfo.class)
                .set(javaGetter(UserInfo::getUid), _ID1)
                .set(javaGetter(UserInfo::getUserName), _CHANGED_USER_NAME)
                .sample();
        UserInfoDto userInfoDto = fixtureMonkey.giveMeBuilder(UserInfoDto.class)
                .set(javaGetter(UserInfoDto::getUserName), _USER_NAME)
                .sample();
        given(mysqlService.modifyUser(anyInt(), any())).willReturn(userInfo);

        mvc.perform(put("/user/{id}", _ID1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(_MAPPER.writeValueAsString(userInfoDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(_CHANGED_USER_NAME)));
        verify(mysqlService, times(1)).modifyUser(anyInt(), any());
    }

    @Test
    @DisplayName("사용자 삭제")
    void removeUser() throws Exception {
        willDoNothing().given(mysqlService).removeUser(anyInt());

        mvc.perform(delete("/user/{id}", _ID1))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("success")));
        verify(mysqlService, times(1)).removeUser(anyInt());
    }

}
