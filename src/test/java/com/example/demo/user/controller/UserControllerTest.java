package com.example.demo.user.controller;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는특정유저의정보를개인정보는소거된채전달받을수있다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("doydoit@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("doydoit"))
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
        ;
    }

    @Test
    void 사용자는존재하지않는유저의아이디로API호출할경우404응답을받는다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/users/123456"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 123456를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는인증코드로계정을활성화할수있다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/users/2/verify").queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .andExpect(status().isFound());

        UserEntity entity = userRepository.findById(2L).get();

        assertThat(entity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는인증코드가일치하지않을경우권한없음에러를내려준다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/users/2/verify").queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac"))
                .andExpect(status().isForbidden());
    }

    @Test
    void 사용자는내정보를불러올때개인정보인주소도갖고올수있다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(
                        get("/api/users/me")
                                .header("EMAIL", "doydoit@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("doydoit@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("doydoit"))
                .andExpect(jsonPath("$.address").value("Seoul"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

    }

    @Test
    void 사용자는내정보를수정할수있다() throws Exception {
        // given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("doydoit-n")
                .address("pangyo")
                .build();

        // when
        // then
        mockMvc.perform(
                put("/api/users/me")
                        .header("EMAIL", "doydoit@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("doydoit@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("doydoit-n"))
                .andExpect(jsonPath("$.address").value("pangyo"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}