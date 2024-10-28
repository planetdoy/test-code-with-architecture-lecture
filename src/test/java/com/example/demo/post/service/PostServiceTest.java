package com.example.demo.post.service;

import com.example.demo.mock.*;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;

class PostServiceTest {

    private PostService postService;
    @BeforeEach
    void init() {
        FakeMailsender fakeMailsender = new FakeMailsender();
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.postService = PostService.builder()
                .postRepository(fakePostRepository)
                .userRepository(fakeUserRepository)
                .clockHolder(new TestClockHolder(1679530673958L))
                .build();

        User user1 = User.builder()
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build();

        User user2 = User.builder()
                .email("doydoit2@gmail.com")
                .nickname("doydoit2")
                .address("Seoul")
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build();

        fakeUserRepository.save(user1);

        fakeUserRepository.save(user2);

        Post post = Post.builder()
                .content("helloworld")
                .createdAt(1678530673958L)
                .modifiedAt(0L)
                .writer(user1)
                .build();

        fakePostRepository.save(post);
    }
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
        assertThat(result.getCreatedAt()).isEqualTo(1679530673958L);

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
        assertThat(post.getModifiedAt()).isEqualTo(1679530673958L);

        // when 직접작성
//        PostEntity result = postService.update(1, postUpdateDto);

        // then 직접작성
//        assertThat(result.getId()).isEqualTo(1);
//        assertThat(result.getContent()).isEqualTo("update-contents");
//        assertThat(result.getModifiedAt()).isGreaterThan(0);
    }
}