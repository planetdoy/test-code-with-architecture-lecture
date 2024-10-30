package com.example.demo.user.controller.port;

/**
 * packageName    : com.example.demo.user.controller.port
 * fileName      : CertificationService
 * owner         : (주)Cobosys
 * date          : 10/30/24 8:01 AM
 * description   :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 10/30/24           doy            최초 생성
 */
public interface CertificationService {
    void send(String email, long id, String certificationCode);
}
