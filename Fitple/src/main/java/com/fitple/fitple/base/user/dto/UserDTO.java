package com.fitple.fitple.base.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String nickname;
}
