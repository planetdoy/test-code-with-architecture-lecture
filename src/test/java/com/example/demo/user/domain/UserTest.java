package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void User는UserCreate객체로생성할수있다() {

        UserCreate userCreate = UserCreate.builder()
                .email("doydoit22@gmail.com")
                .nickname("doydoit22")
                .address("gayang")
                .build();

        User user = User.from(userCreate, new TestUuidHolder("aaa-aa-aaaaa"));

        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("doydoit22@gmail.com");
        assertThat(user.getNickname()).isEqualTo("doydoit22");
        assertThat(user.getAddress()).isEqualTo("gayang");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaa-aa-aaaaa");

    }

    @Test
    public void User는UserUpdate객체로데이터를update할수있다() {
        User user = User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaa-aa-aaaaa")
                .build();

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("doydoit22")
                .address("gayang")
                .build();

        user = user.update(userUpdate);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("doydoit@gmail.com");
        assertThat(user.getNickname()).isEqualTo("doydoit22");
        assertThat(user.getAddress()).isEqualTo("gayang");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
        assertThat(user.getCertificationCode()).isEqualTo("aaa-aa-aaaaa");

    }

    @Test
    public void User는로그인을할수있고로그인시마지막로그인시간이변경됀다() {
        User user = User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaa-aa-aaaaa")
                .build();

        user = user.login(new TestClockHolder(1234561234567L));

        assertThat(user.getLastLoginAt()).isEqualTo(1234561234567L);
    }

    @Test
    public void User는유효한인증코드로계정을활성화할수있다() {
        User user = User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaa-aa-aaaaa")
                .build();

        user = user.certificate("aaa-aa-aaaaa");

        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @Test
    public void User는잘못된인증코드로계정을활성화하려하면에러를던진다() {
        User user = User.builder()
                .id(1L)
                .email("doydoit@gmail.com")
                .nickname("doydoit")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaa-aa-aaaaa")
                .build();

        assertThatThrownBy(() -> user.certificate("aaa-aa-aaaab"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);

    }

}