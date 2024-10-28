package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PostTest {

    @Test
    public void PostCreate으로게시물을만들수있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1L)
                .content("helloworld")
                .build();

        User user = User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaa-aa-aaaaa")
                .build();

        // when
        Post post = Post.from(postCreate, user, new TestClockHolder(1679530673958L));

        // then
        assertThat(post.getContent()).isEqualTo("helloworld");
        assertThat(post.getCreatedAt()).isEqualTo(1679530673958L);
        assertThat(post.getWriter().getEmail()).isEqualTo("doydoit@gmail.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("doydoit");
        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaa-aa-aaaaa");
    }

    @Test
    public void PostUpdate로게시물을수정할수있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1L)
                .content("helloworld")
                .build();

        PostUpdate postUpdate = PostUpdate.builder()
                .content("Hello World")
                .build();

        User user = User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaa-aa-aaaaa")
                .build();

        // when
        Post post = Post.from(postCreate, user, new TestClockHolder(1678530673958L));
        post = post.update(postUpdate, new TestClockHolder(1679530673958L));

        // then
        assertThat(post.getContent()).isEqualTo("Hello World");
        assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);
        assertThat(post.getModifiedAt()).isEqualTo(1679530673958L);
        assertThat(post.getWriter().getEmail()).isEqualTo("doydoit@gmail.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("doydoit");
        assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaa-aa-aaaaa");
    }

}