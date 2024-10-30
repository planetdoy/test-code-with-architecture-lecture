package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailsender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UserServiceTest {
    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        FakeMailsender fakeMailsender = new FakeMailsender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.userService = UserServiceImpl.builder()
                .uuidHolder(new TestUuidHolder("aaa-aa-aaaaa"))
                .clockHolder(new TestClockHolder(1678530673958L))
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMailsender))
                .build();

        fakeUserRepository.save(User.builder()
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build());

        fakeUserRepository.save(User.builder()
                .email("doydoit2@gmail.com")
                .nickname("doydoit2")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());

    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        String email = "doydoit@gmail.com";
        User result = userService.getByEmail(email);
        assertThat(result.getNickname()).isEqualTo("doydoit");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저는_찾아올_수_없다() {
        String email = "doydoit2@gmail.com";

        assertThatThrownBy(() -> {
            User result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        User result = userService.getById(1);
        assertThat(result.getNickname()).isEqualTo("doydoit");
    }

    @Test
    void getById은_PENDING_상태인_유저는_찾아올_수_없다() {
        assertThatThrownBy(() -> {
            User result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreate_를_이용하여_유저를_생성할_수_있다() {
        UserCreate userCreate = UserCreate.builder()
                .email("doydoit@gmail.com")
                .address("gayang")
                .nickname("doydoit")
                .build();

        User result = userService.create(userCreate);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo("aaa-aa-aaaaa");
    }

    @Test
    void userUpdate_를_이용하여_유저를_수정할_수_있다() {
        UserUpdate userUpdate = UserUpdate.builder()
                .address("gayang-2")
                .nickname("doydoit-2")
                .build();

        userService.update(1, userUpdate);

        User result = userService.getById(1);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getAddress()).isEqualTo("gayang-2");
        assertThat(result.getNickname()).isEqualTo("doydoit-2");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        userService.login(1);

        User result = userService.getById(1);
        assertThat(result.getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

        User result = userService.getById(2);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}