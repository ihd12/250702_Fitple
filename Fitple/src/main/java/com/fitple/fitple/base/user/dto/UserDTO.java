package com.fitple.fitple.base.user.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String password;  // 클라이언트에서 보내는 비밀번호
    private String nickname;
}
