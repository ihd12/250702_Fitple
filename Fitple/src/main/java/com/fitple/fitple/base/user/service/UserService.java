package com.fitple.fitple.base.user.service;

import com.fitple.fitple.base.user.dto.UserDTO;

public interface UserService {

    // 회원가입 처리 메서드
    UserDTO register(UserDTO dto);

}
