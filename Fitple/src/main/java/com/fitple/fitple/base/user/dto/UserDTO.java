package com.fitple.fitple.base.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private Long id;
    private String email;
    private String nickname;
    private String password;

    // User → UserDTO 변환용 정적 메서드
    public static UserDTO toDTO(com.fitple.fitple.base.user.domain.User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
