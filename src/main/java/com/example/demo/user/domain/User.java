package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;

    private String email;

    private String nickname;

    private String address;

    private String certificationCode;

    private UserStatus status;

    private Long lastLoginAt;

    public static User from(UserCreate userCreate) {
        return User.builder()
                .email(userCreate.getEmail())
                .nickname(userCreate.getNickname())
                .address(userCreate.getAddress())
                .status(UserStatus.PENDING)
                .certificationCode(UUID.randomUUID().toString())
                .build();
    }

    public User update(UserUpdate userUpdate) {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .nickname(userUpdate.getNickname())
                .address(userUpdate.getAddress())
                .certificationCode(this.certificationCode)
                .status(this.status)
                .lastLoginAt(this.lastLoginAt)
                .build();
    }

    public User login() {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .nickname(this.nickname)
                .address(this.address)
                .certificationCode(this.certificationCode)
                .status(this.status)
                .lastLoginAt(Clock.systemUTC().millis())
                .build();
    }

    public User certificate(String certificationCode) {
        if (!this.certificationCode.equals(certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }

        return User.builder()
                .id(this.id)
                .email(this.email)
                .nickname(this.nickname)
                .address(this.address)
                .certificationCode(this.certificationCode)
                .status(UserStatus.ACTIVE)
                .lastLoginAt(this.lastLoginAt)
                .build();

    }
}