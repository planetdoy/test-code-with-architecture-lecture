package com.example.demo.post.domain;

import com.example.demo.common.service.port.ClockHolder;
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

    public static Post from(PostCreate postCreate, User user, ClockHolder clockHolder) {
        return Post.builder()
                .writer(user)
                .content(postCreate.getContent())
                .createdAt(clockHolder.millis())
                .build();
    }

    public Post update(PostUpdate postUpdate, ClockHolder clockHolder) {
        return Post.builder()
                .id(this.id)
                .content(postUpdate.getContent())
                .createdAt(this.createdAt)
                .modifiedAt(clockHolder.millis())
                .writer(this.writer)
                .build();
    }
}
