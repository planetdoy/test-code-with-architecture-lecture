package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import com.example.demo.user.infrastructure.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Clock;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    private Long id;

    private String content;

    private Long createdAt;

    private Long modifiedAt;

    private User writer;

    public static Post from(PostCreate postCreate, User user) {
        return Post.builder()
                .writer(user)
                .content(postCreate.getContent())
                .createdAt(Clock.systemUTC().millis())
                .build();
    }

    public Post update(PostUpdate postUpdate) {
        return Post.builder()
                .id(this.id)
                .content(postUpdate.getContent())
                .createdAt(this.createdAt)
                .modifiedAt(Clock.systemUTC().millis())
                .writer(this.writer)
                .build();
    }
}
