package com.fitple.fitple.base.user.service;

import com.fitple.fitple.base.user.domain.User;
import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.base.user.repository.UserRepository;
import com.fitple.fitple.common.dto.PageRequestDTO;
import com.fitple.fitple.common.dto.PageResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        Page<User> result = userRepository.searchUser(keyword, pageable);

        List<UserDTO> dtoList = result.getContent()
                .stream()
                .map(UserDTO::toDTO)
                .toList();

        System.out.println("검색어: " + keyword);
        System.out.println("전체 회원 수: " + result.getTotalElements());
        System.out.println("가져온 회원 리스트: " + dtoList);

        return new PageResponseDTO<>(pageRequestDTO, dtoList, (int) result.getTotalElements());
    }


    @Override
    public UserDTO getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("not found : "+email));
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
            user.changePassword(passwordEncoder.encode(dto.getPassword()));
        }
    }

    @Override
    public void delete(String email) {
        userRepository.deleteByEmail(email);
    }

    private User dtoToEntity(UserDTO dto) {
        return User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .auth("USER")
                .build();
    }

}