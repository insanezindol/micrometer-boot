package com.example.micrometerboot.service;

import com.example.micrometerboot.dto.UserInfoDto;
import com.example.micrometerboot.mysql.entity.UserInfo;
import com.example.micrometerboot.mysql.repository.StudentRepository;
import com.example.micrometerboot.mysql.repository.UserInfoRepository;
import com.navercorp.fixturemonkey.FixtureMonkey;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MysqlServiceTest {

    /**
     * 1. Stubbing
     * Mock 객체에  원하는 동작을 미리 지정해주는 것을 stub라고 한다.
     * 여러가지 stub 있겠지만 대표적으로 when을 많이 사용한다.
     * <p>
     * 2. Stubbing Verify
     * Mock 객체의 특정 행위가 몇번 호출되었는지, 추가적으로 interaction이 발생되었는지 여부등도 확인이 가능하다.
     * <p>
     * 3. BDD(Behaviour-Driven Development) Mockito
     * BDD(Behaviour-Driven Development)는 행동 기반 테스트인데 Mockito에서 제공하는 기능들을 이용하면 Given / When / Then 순서대로 검증이 가능하다.
     * when이라는 subbing 메서드와 동일한 역할을 하는 given은 BDD를 위해서 when 대신 Given으로 사용한다.
     * then을 통해서 검증이 가능하다.
     */

    static final int _ID1 = 1;
    static final int _ID2 = 2;
    static final String _USER_NAME_1 = "JINHYUNGLEE";
    static final String _USER_NAME_2 = "DEAN";

    @InjectMocks
    MysqlService mysqlService;

    @Mock
    UserInfoRepository userInfoRepository;

    @Mock
    StudentRepository studentRepository;

    FixtureMonkey fixtureMonkey;

    @BeforeEach
    void setup() {
        this.fixtureMonkey = FixtureMonkey.create();
    }

    @Test
    @DisplayName("사용자 전체 검색")
    void findAllUser() {
        // given
        final int docSize = 5;
        List<UserInfo> userInfos = fixtureMonkey.giveMe(UserInfo.class, docSize);
        when(userInfoRepository.findAll()).thenReturn(userInfos);

        // when
        List<UserInfo> list = mysqlService.findAllUser();

        // then
        assertNotNull(list);
        assertEquals(docSize, list.size());
        verify(userInfoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("사용자 아이디 검색")
    void findUserById() {
        // given
        UserInfo userInfo = fixtureMonkey.giveMeBuilder(UserInfo.class)
                .set(javaGetter(UserInfo::getUid), _ID1)
                .sample();
        when(userInfoRepository.findById(anyInt())).thenReturn(Optional.of(userInfo));

        // when
        UserInfo user = mysqlService.findUserById(_ID1);

        // then
        assertNotNull(user);
        assertEquals(_ID1, user.getUid());
        verify(userInfoRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("사용자 이름 검색")
    void findUserByUserName() {
        // given
        UserInfo userInfo1 = fixtureMonkey.giveMeBuilder(UserInfo.class)
                .set(javaGetter(UserInfo::getUid), _ID1)
                .set(javaGetter(UserInfo::getUserName), _USER_NAME_1)
                .sample();
        UserInfo userInfo2 = fixtureMonkey.giveMeBuilder(UserInfo.class)
                .set(javaGetter(UserInfo::getUid), _ID2)
                .set(javaGetter(UserInfo::getUserName), _USER_NAME_1)
                .sample();
        when(userInfoRepository.findByUserName(any())).thenReturn(Arrays.asList(userInfo1, userInfo2));

        // when
        List<UserInfo> list = mysqlService.findUserByUserName(_USER_NAME_1);

        // then
        assertNotNull(list);
        assertEquals(2, list.size());
        assertThat(list).filteredOn("uid", _ID1).containsOnly(userInfo1);
        assertThat(list).filteredOn("uid", _ID2).containsOnly(userInfo2);
        assertThat(list).extracting("userName").contains(_USER_NAME_1);
        assertThat(list).extracting("uid", "userName")
                .contains(tuple(_ID1, _USER_NAME_1), tuple(_ID2, _USER_NAME_1));
        verify(userInfoRepository, times(1)).findByUserName(any());
    }

    @Test
    @DisplayName("사용자 추가")
    void addUser() {
        // given
        UserInfo userInfo = fixtureMonkey.giveMeBuilder(UserInfo.class)
                .set(javaGetter(UserInfo::getUid), _ID1)
                .set(javaGetter(UserInfo::getUserName), _USER_NAME_1)
                .sample();
        UserInfoDto userInfoDto = fixtureMonkey.giveMeBuilder(UserInfoDto.class)
                .set(javaGetter(UserInfoDto::getUserName), _USER_NAME_1)
                .sample();
        when(userInfoRepository.save(any())).thenReturn(userInfo);

        // when
        UserInfo savedUser = mysqlService.addUser(userInfoDto);

        // then
        assertNotNull(savedUser);
        assertEquals(userInfo.getUserName(), savedUser.getUserName());
        verify(userInfoRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("사용자 추가 Mockito")
    void addUserBdd() {
        // given
        UserInfo userInfo = fixtureMonkey.giveMeBuilder(UserInfo.class)
                .set(javaGetter(UserInfo::getUid), _ID1)
                .set(javaGetter(UserInfo::getUserName), _USER_NAME_1)
                .sample();
        UserInfoDto userInfoDto = fixtureMonkey.giveMeBuilder(UserInfoDto.class)
                .set(javaGetter(UserInfoDto::getUserName), _USER_NAME_1)
                .sample();
        given(userInfoRepository.save(any())).willReturn(userInfo);

        // when
        UserInfo savedUser = mysqlService.addUser(userInfoDto);

        // then
        assertNotNull(savedUser);
        assertEquals(userInfo.getUserName(), savedUser.getUserName());
        then(userInfoRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("사용자 수정")
    void modifyUser() {
        // given
        UserInfo userInfo = fixtureMonkey.giveMeBuilder(UserInfo.class)
                .set(javaGetter(UserInfo::getUid), _ID1)
                .set(javaGetter(UserInfo::getUserName), _USER_NAME_1)
                .sample();
        UserInfoDto userInfoDto = fixtureMonkey.giveMeBuilder(UserInfoDto.class)
                .set(javaGetter(UserInfoDto::getUserName), _USER_NAME_2)
                .sample();
        when(userInfoRepository.findById(any())).thenReturn(Optional.of(userInfo));

        // when
        UserInfo savedUser = mysqlService.modifyUser(_ID1, userInfoDto);

        // then
        assertNotNull(savedUser);
        assertEquals(_USER_NAME_2, savedUser.getUserName());
        verify(userInfoRepository, times(1)).findById(any());
        verify(userInfoRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("사용자 삭제")
    void removeUser() {
        // given
        UserInfo userInfo = fixtureMonkey.giveMeOne(UserInfo.class);
        when(userInfoRepository.findById(any())).thenReturn(Optional.of(userInfo));

        // when
        mysqlService.removeUser(_ID1);

        // then
        verify(userInfoRepository, times(1)).findById(any());
        verify(userInfoRepository, times(1)).delete(any());
    }

}
