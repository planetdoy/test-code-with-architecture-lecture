package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.port.UserService;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import com.example.demo.user.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Test
    void 사용자는특정유저의정보를개인정보는소거된채전달받을수있다() throws Exception {
        TestContainer testContainer = TestContainer.builder()
                .build();

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

//    @Test
//    void 사용자는존재하지않는유저의아이디로API호출할경우404응답을받는다() throws Exception {
//        // given
//        // when
//        // then
//
//    }
//
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
//
//    @Test
//    void 사용자는인증코드가일치하지않을경우권한없음에러를내려준다() throws Exception {
//        // given
//        // when
//        // then
//        mockMvc.perform(get("/api/users/2/verify").queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac"))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    void 사용자는내정보를불러올때개인정보인주소도갖고올수있다() throws Exception {
//        // given
//        // when
//        // then
//        mockMvc.perform(
//                        get("/api/users/me")
//                                .header("EMAIL", "doydoit@gmail.com"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.email").value("doydoit@gmail.com"))
//                .andExpect(jsonPath("$.nickname").value("doydoit"))
//                .andExpect(jsonPath("$.address").value("Seoul"))
//                .andExpect(jsonPath("$.status").value("ACTIVE"));
//
//    }
//
//    @Test
//    void 사용자는내정보를수정할수있다() throws Exception {
//        // given
//        UserUpdate userUpdate = UserUpdate.builder()
//                .nickname("doydoit-n")
//                .address("pangyo")
//                .build();
//
//        // when
//        // then
//        mockMvc.perform(
//                put("/api/users/me")
//                        .header("EMAIL", "doydoit@gmail.com")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userUpdate)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value("doydoit@gmail.com"))
//                .andExpect(jsonPath("$.nickname").value("doydoit-n"))
//                .andExpect(jsonPath("$.address").value("pangyo"))
//                .andExpect(jsonPath("$.status").value("ACTIVE"));
//    }
}