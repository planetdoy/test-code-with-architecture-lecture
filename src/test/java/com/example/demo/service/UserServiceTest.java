package com.example.demo.service;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    private UserService userService;

    // @Autowired
    // java bean 으로 등록된 객체를 Mock으로 선언된 객체로 덮어쓰기
    // 이러면 테스트를 실행할 때 MockBean 값이 주입되어 실행됩니다.
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        String email = "doydoit@gmail.com";
        UserEntity result = userService.getByEmail(email);
        assertThat(result.getNickname()).isEqualTo("doydoit");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저는_찾아올_수_없다() {
        String email = "doydoit2@gmail.com";

        assertThatThrownBy(() -> {
            UserEntity result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        UserEntity result = userService.getById(1);
        assertThat(result.getNickname()).isEqualTo("doydoit");
    }

    @Test
    void getById은_PENDING_상태인_유저는_찾아올_수_없다() {
        assertThatThrownBy(() -> {
            UserEntity result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto_를_이용하여_유저를_생성할_수_있다() {
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("doydoit@gmail.com")
                .address("gayang")
                .nickname("doydoit")
                .build();

        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        UserEntity result = userService.create(userCreateDto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo("T.T");
    }

    @Test
    void userUpdateDto_를_이용하여_유저를_수정할_수_있다() {
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .address("gayang-2")
                .nickname("doydoit-2")
                .build();

        userService.update(1, userUpdateDto);

        UserEntity result = userService.getById(1);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getAddress()).isEqualTo("gayang-2");
        assertThat(result.getNickname()).isEqualTo("doydoit-2");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        userService.login(1);

        UserEntity result = userService.getById(1);
        assertThat(result.getLastLoginAt()).isGreaterThan(0L);
//        assertThat(result.getLastLoginAt()).isEqualTo("T.T"); // FIXME
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

        UserEntity result = userService.getById(2);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}