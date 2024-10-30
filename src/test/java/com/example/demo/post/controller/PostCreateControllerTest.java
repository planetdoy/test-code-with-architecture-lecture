package com.example.demo.post.controller;

import com.example.demo.post.domain.PostCreate;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostCreateControllerTest {

//    @Test
//    void 사용자는게시물을생성할수있습니다() throws Exception {
//        //given
//        PostCreate postCreate = PostCreate.builder()
//                .writerId(1L)
//                .content("hello, world")
//                .build();
//
//        //when
//        //then
//        mockMvc.perform(
//                post("/api/posts")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(postCreate))
//                )
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.content").value("hello, world"))
//                .andExpect(jsonPath("$.writer.id").value(1L))
//                .andExpect(jsonPath("$.writer.email").value("doydoit@gmail.com"))
//                .andExpect(jsonPath("$.writer.nickname").value("doydoit"))
//                .andExpect(jsonPath("$.writer.id").value(1L));
//    }
}