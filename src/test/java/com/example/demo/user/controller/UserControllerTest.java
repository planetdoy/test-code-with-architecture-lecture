package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class UserControllerTest {

    @Test
    void 사용자는특정유저의정보를개인정보는소거된채전달받을수있다() throws Exception {
        TestContainer testContainer = TestContainer.builder().build();

        // given
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaa-aa-aaaaa")
                .lastLoginAt(100L)
                .build());

        // when
        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("doydoit@gmail.com");
        assertThat(result.getBody().getNickname()).isEqualTo("doydoit");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는존재하지않는유저의아이디로API호출할경우404응답을받는다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();

        // when
        // then
        assertThatThrownBy(() -> testContainer.userController.getUserById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는인증코드로계정을활성화할수있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaa-aa-aaaaa")
                .lastLoginAt(100L)
                .build());
        // when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1, "aaa-aa-aaaaa");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.getById(1).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는인증코드가일치하지않을경우권한없음에러를내려준다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();
        // when
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .certificationCode("aaa-aa-aaaaa")
                .lastLoginAt(100L)
                .build());
        // then
        assertThatThrownBy(() -> testContainer.userController.verifyEmail(1, "aaa-aa-aaaab"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 사용자는내정보를불러올때개인정보인주소도갖고올수있다()  {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(100L))
                .build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaa-aa-aaaaa")
                .lastLoginAt(100L)
                .build());

        // when
        ResponseEntity<MyProfileResponse> response = testContainer.userController.getMyInfo("doydoit@gmail.com");

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("doydoit@gmail.com");
        assertThat(response.getBody().getNickname()).isEqualTo("doydoit");
        assertThat(response.getBody().getLastLoginAt()).isEqualTo(100L);
        assertThat(response.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는내정보를수정할수있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder().build();

        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaa-aa-aaaaa")
                .lastLoginAt(100L)
                .build());

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("doydoit-n")
                .address("pangyo")
                .build();

        // when
        ResponseEntity<MyProfileResponse> response
                = testContainer.userController.updateMyInfo("doydoit@gmail.com", userUpdate);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("doydoit@gmail.com");
        assertThat(response.getBody().getNickname()).isEqualTo("doydoit-n");
        assertThat(response.getBody().getAddress()).isEqualTo("pangyo");
        assertThat(response.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
}