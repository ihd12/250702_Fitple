package com.fitple.fitple.base.user.controller;

import com.fitple.fitple.base.user.dto.UserDTO;
import com.fitple.fitple.base.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;


    // 로그인 폼
    @GetMapping("/login")
    public String loginForm() {
        return "user/login";  // templates/user/login.html
    }

    // 회원가입 폼
    @GetMapping("/register")
    public String registerForm() {
        return "user/register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String register(UserDTO dto) {
        userService.register(dto);
        return "redirect:/user/login";
    }

    // 로그인/로그아웃 POST/GET 메서드는 Spring Security가 처리하므로 삭제함
}
