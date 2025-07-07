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

    @Override
    public UserDTO register(UserDTO dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .build();

        User saved = userRepository.save(user);

        return UserDTO.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .nickname(saved.getNickname())
                .build();
    }
}
