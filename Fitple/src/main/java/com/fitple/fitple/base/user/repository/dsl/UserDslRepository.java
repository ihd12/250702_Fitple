package com.fitple.fitple.base.user.repository.dsl;

import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.common.dto.PageResponseDTO;

public interface UserDslRepository {
    PageResponseDTO<UserDTO> searchUser(PageRequestDTO pageRequestDTO);
}
