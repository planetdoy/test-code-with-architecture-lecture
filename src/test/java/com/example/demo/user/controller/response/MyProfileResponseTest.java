package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MyProfileResponseTest {

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

        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        assertThat(myProfileResponse.getId()).isEqualTo(user.getId());
        assertThat(myProfileResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(myProfileResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(myProfileResponse.getAddress()).isEqualTo(user.getAddress());
        assertThat(myProfileResponse.getStatus()).isEqualTo(user.getStatus());
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(user.getLastLoginAt());
    }

}