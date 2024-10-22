package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PostResponseTest {

    @Test
    public void Post로응답을생성할수있다() {
        Post post = Post.builder()
                .content("helloworld")
                .writer(User.builder()
                        .id(1L)
                        .email("doydoit@gmail.com")
                        .nickname("doydoit")
                        .address("Seoul")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaa-aa-aaaaa")
                        .build())
                .build();

        PostResponse postResponse = PostResponse.from(post);

        assertThat(postResponse.getContent()).isEqualTo("helloworld");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("doydoit@gmail.com");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("doydoit");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);

    }


}