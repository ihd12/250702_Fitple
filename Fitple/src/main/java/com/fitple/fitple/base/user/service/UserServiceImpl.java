package com.fitple.fitple.base.user.service;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.base.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 처리
    @Override
    public UserDTO register(UserDTO dto) {
        User user = new User(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);

        return UserDTO.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .nickname(saved.getNickname())
                .build();
    }
}
