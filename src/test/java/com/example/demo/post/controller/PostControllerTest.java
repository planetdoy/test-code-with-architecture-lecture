package com.example.demo.post.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest {

    @Test
    void 게시물ID로게시물을조회할수있다() {
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(100L))
                .build();

        //given
        User user = testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaa-aa-aaaaa")
                .lastLoginAt(100L)
                .build());

        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("hello world")
                .createdAt(100L)
                .writer(user)
                .build());

        // when
        ResponseEntity<PostResponse> response = testContainer.postController.getPostById(1L);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getContent()).isEqualTo("hello world");
        assertThat(response.getBody().getCreatedAt()).isEqualTo(100L);
        assertThat(response.getBody().getWriter().getId()).isEqualTo(1L);
    }

    @Test
    void 사용자가존재하지않는게시물을조회할경우에러가난다() {
        // given
        TestContainer testContainer = TestContainer.builder().build();

        // when
        // then
        assertThatThrownBy(() -> {
            testContainer.postController.getPostById(1L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 게시물id로게시물내용을수정할수있다() {

        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 200L)
                .build();

        //given
        User user = testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaa-aa-aaaaa")
                .lastLoginAt(100L)
                .build());

        testContainer.postRepository.save(Post.builder()
                .id(1L)
                .content("hello world")
                .createdAt(100L)
                .writer(user)
                .build());

        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello world!!")
                .build();

        //when
        ResponseEntity<PostResponse> response = testContainer.postController.updatePost(1L, postUpdate);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getContent()).isEqualTo("hello world!!");
        assertThat(response.getBody().getCreatedAt()).isEqualTo(100L);
        assertThat(response.getBody().getModifiedAt()).isEqualTo(200L);
        assertThat(response.getBody().getWriter().getNickname()).isEqualTo("doydoit");
    }
}