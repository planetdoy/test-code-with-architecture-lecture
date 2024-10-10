package com.example.demo.user.service;

import com.example.demo.mock.FakeMailsender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CertificationServiceTest {

    @Test
    void 이메일과컨텐츠가제대로만들어져서보내지는지테스트한다() {

        // given
        FakeMailsender fakeMailsender = new FakeMailsender();

        CertificationService certificationService = new CertificationService(fakeMailsender);

        // when
        certificationService.send("doydoit@gmail.com", 1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        Assertions.assertThat(fakeMailsender.email).isEqualTo("doydoit@gmail.com");
        Assertions.assertThat(fakeMailsender.title).isEqualTo("Please certify your email address");
        Assertions.assertThat(fakeMailsender.content).isEqualTo("Please click the following link to certify your email address: " + "http://localhost:8080/api/users/" + 1 + "/verify?certificationCode=" + "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

    @Test
    void generateCertificationUrl() {
    }
}