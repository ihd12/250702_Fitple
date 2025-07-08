package com.fitple.fitple.base.user.service;

import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.common.dto.PageResponseDTO;

public interface UserService {
    UserDTO register(UserDTO dto);
    PageResponseDTO<UserDTO> getUserList(PageRequestDTO pageRequestDTO);
    UserDTO getUser(String email);
    void modify(UserDTO dto);
    void delete(String email);
}
