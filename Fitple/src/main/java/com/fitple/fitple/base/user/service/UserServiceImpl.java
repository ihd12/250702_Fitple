package com.fitple.fitple.base.user.service;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.base.user.repository.UserRepository;
import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.common.dto.PageResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO register(UserDTO dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .auth("USER")
                .build();

        User saved = userRepository.save(user);

        return UserDTO.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .nickname(saved.getNickname())
                .build();
    }

    @Override
    public PageResponseDTO<UserDTO> getUserList(PageRequestDTO pageRequestDTO) {
        return userRepository.searchUser(pageRequestDTO);
    }

    @Override
    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("not found"+id));
        return UserDTO.toDTO(user);
    }


    @Override
    public void modify(UserDTO dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(()->new IllegalArgumentException("not found"+dto.getId()));
        if(dto.getNickname()!=null && !dto.getNickname().isBlank()){
            user.changeNickname(dto.getNickname());
        }
        if(dto.getPassword()!=null && !dto.getPassword().isBlank()){
            user.changePassword(dto.getPassword());
        }
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
