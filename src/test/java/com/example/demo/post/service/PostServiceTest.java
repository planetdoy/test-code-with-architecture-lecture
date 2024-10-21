package com.example.demo.post.service;

import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import com.example.demo.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        , @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void getById는_존재하는_게시물을_내려준다() {
        // given
        // when
        Post result = postService.getById(1);

        // then
        assertThat(result.getContent()).isEqualTo("helloworld");
        assertThat(result.getWriter().getEmail()).isEqualTo("doydoit@gmail.com");

        // then 직접작성
//        assertThat(result).isNotNull();
    }

    @Test
    void postCreateDto_를_이용하여_게시물을_생성할_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("contents")
                .build();
        // when
        Post result = postService.create(postCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("contents");
        assertThat(result.getCreatedAt()).isGreaterThan(0);

        // then 직접작성
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isGreaterThan(1);
//        assertThat(result.getCreatedAt()).isGreaterThan(0);
//        assertThat(result.getModifiedAt()).isNull();
//        assertThat(result.getWriter().getId()).isEqualTo(1L);
    }

    @Test
    void postUpdateDto_를_이용하여_게시물을_수정할_수_있다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("update-contents")
                .build();

        // when
        postService.update(1, postUpdate);

        //then
        Post post = postService.getById(1);
        assertThat(post.getContent()).isEqualTo("update-contents");
        assertThat(post.getModifiedAt()).isGreaterThan(0);

        // when 직접작성
//        PostEntity result = postService.update(1, postUpdateDto);

        // then 직접작성
//        assertThat(result.getId()).isEqualTo(1);
//        assertThat(result.getContent()).isEqualTo("update-contents");
//        assertThat(result.getModifiedAt()).isGreaterThan(0);
    }
}