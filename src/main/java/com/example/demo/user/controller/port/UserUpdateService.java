package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;

/**
 * packageName    : com.example.demo.user.controller.port
 * fileName      : UserService
 * owner         : (주)Cobosys
 * date          : 10/30/24 8:01 AM
 * description   :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 10/30/24           doy            최초 생성
 */
public interface UserUpdateService {

    User update(long id, UserUpdate userUpdate);
}
