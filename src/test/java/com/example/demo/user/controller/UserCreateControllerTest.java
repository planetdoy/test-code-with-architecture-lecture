package com.example.demo.user.controller;

import com.example.demo.user.domain.UserCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserCreateControllerTest {

//    @Test
//    void 사용자는회원가입을할수있고회원가입된사용자는PENDING상태이다() throws Exception {
//        // given
//        UserCreate userCreate = UserCreate.builder()
//                .email("doydoit@gmail.com")
//                .nickname("doydoit-n")
//                .address("pangyo")
//                .build();
//        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
//
//        // when
//        // then
//        mockMvc.perform(
//                        post("/api/users")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(userCreate)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.email").value("doydoit@gmail.com"))
//                .andExpect(jsonPath("$.nickname").value("doydoit-n"))
//                .andExpect(jsonPath("$.status").value("PENDING"));
//    }

}