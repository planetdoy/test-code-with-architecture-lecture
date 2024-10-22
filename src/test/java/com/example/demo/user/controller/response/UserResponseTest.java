package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void User으로응답을생성할수있다() {
        User user = User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaa-aa-aaaaa")
                .build();

        UserResponse userResponse = UserResponse.from(user);

        assertThat(userResponse.getId()).isEqualTo(user.getId());
        assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(userResponse.getStatus()).isEqualTo(user.getStatus());
        assertThat(userResponse.getLastLoginAt()).isEqualTo(user.getLastLoginAt());
    }
}